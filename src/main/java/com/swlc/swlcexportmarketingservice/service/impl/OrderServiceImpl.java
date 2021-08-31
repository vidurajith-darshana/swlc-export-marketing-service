package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.*;
import com.swlc.swlcexportmarketingservice.dto.common.CommonOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.common.CommonResponseDTO;
import com.swlc.swlcexportmarketingservice.dto.response.FullOrderDTO;
import com.swlc.swlcexportmarketingservice.dto.response.FullOrderDetailsDTO;
import com.swlc.swlcexportmarketingservice.entity.*;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.mapper.OrderDetailMapper;
import com.swlc.swlcexportmarketingservice.mapper.OrderMapper;
import com.swlc.swlcexportmarketingservice.repository.*;
import com.swlc.swlcexportmarketingservice.service.OrderService;
import com.swlc.swlcexportmarketingservice.util.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.swlc.swlcexportmarketingservice.constant.ApplicationConstant.*;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private NumberGeneratorRepository numberGeneratorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${Dataentry.operator.mail}")
    private String doEmail;

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    @Override
    public ResponseEntity<CommonResponseDTO> saveOrder(CommonOrderDTO commonOrderDTO) {

        try {
            OrderDto orderDto = new OrderDto(commonOrderDTO.getId(),
                    commonOrderDTO.getFkUserId(),
                    commonOrderDTO.getTotal(), commonOrderDTO.getMessage(), commonOrderDTO.getStatus());

            Order order = orderMapper.convertOrderDtoToOrder(orderDto);
            order.setOrderRef(generateOrderNumber());
            order.setStatus(ORDER_STATUS.REVIEWING.toString());
            order = orderRepository.save(order);
            if (order != null) {
                int orderId = order.getId().intValue();
                for (OrderDetailDto orderDetailDto : commonOrderDTO.getOrderDetails()) {
                    orderDetailDto.setOrderId(orderId);
                    orderDetailRepository.save(orderDetailMapper.convertOrderDetailDtoToOrderDetail(orderDetailDto));
                }

                sendOrderEmail(order.getOrderRef(),commonOrderDTO);

                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Order created successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Order Creation fail!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getAllOrders() {
        try {
            List<Order> orderList = orderRepository.findAll();

            List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
            List<CommonOrderDTO> commonOrderDTOList = new ArrayList<>();

            if (orderList.size() > 0) {
                for (Order order : orderList) {
                    OrderDto orderDto = orderMapper.convertOrderToOrderDto(order);
                    for (OrderDetail orderDetail : order.getFkOrder()) {
                        orderDetailDtoList.add(orderDetailMapper.convertOrderDetailToOrderDetailDto(orderDetail));
                    }
                    CommonOrderDTO commonOrderDTO = new CommonOrderDTO(orderDto.getId(), orderDto.getFkUserId(), orderDto.getTotal(), orderDto.getMessage(), orderDto.getStatus(), orderDetailDtoList);
                    commonOrderDTOList.add(commonOrderDTO);
                }
                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, commonOrderDTOList), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Failed to get all orders!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> trackOrder(String orderRef) {
        try {
            Order order = orderRepository.findOrderByOrderRef(orderRef);

            if (order != null) {
                OrderDto orderDto = orderMapper.convertOrderToOrderDto(order);
                return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, orderDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Order not found with given ref!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> updateOrderStatus(String orderRef, String status) {
        try {
            Order order = orderRepository.findOrderByOrderRef(orderRef);

            if (order != null) {
                if (status.equals(ORDER_STATUS.REVIEWING.toString()) ||
                    status.equals(ORDER_STATUS.IN_PROGRESS.toString()) ||
                    status.equals(ORDER_STATUS.DISPATCHED.toString()) ||
                    status.equals(ORDER_STATUS.DELIVERED.toString())
                )   {
                    order.setStatus(status);
                    orderRepository.save(order);

                    return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, null), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "not a valid status!"), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "Order not found with given ref!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> updateOrder(CommonOrderDTO commonOrderDTO) {
        try {
            Order existingOrder = orderRepository.findById(new Integer(commonOrderDTO.getId())).orElse(null);
            if (existingOrder != null) {
                existingOrder.setStatus(commonOrderDTO.getStatus());
                Order order = orderRepository.save(existingOrder);
                if (order != null) {
                    return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, "Order updated successfully!"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "failed to update order!"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, "unable to find order!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, APPLICATION_ERROR_OCCURRED_MESSAGE, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private synchronized String generateOrderNumber() {
        NumberGenerator numberGenerator = numberGeneratorRepository.findLastNumber();
        int num = 0;
        if (numberGenerator != null) {
            num = numberGenerator.getNumber();
        }

        String ref = "OD";
        if (num >= 10) ref += "000" + num;
        if (num >= 100) ref += "00" + num;
        if (num >= 1000) ref += "0" + num;
        else ref += "0000" + num;

        num = num + 1;
        numberGenerator = new NumberGenerator();
        numberGenerator.setNumber(num);
        numberGeneratorRepository.save(numberGenerator);

        return ref;
    }

    private void sendOrderEmail(String orderRef, CommonOrderDTO commonOrderDTO) {

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\"><head>\n" +
                "<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">\n" +
                "<meta content=\"width=device-width\" name=\"viewport\">\n" +
                "<!--[if !mso]><!-->\n" +
                "<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\">\n" +
                "<!--<![endif]-->\n" +
                "<title></title>\n" +
                "<!--[if !mso]><!-->\n" +
                "<!--<![endif]-->\n" +
                "<style type=\"text/css\">\n" +
                "\t\tbody {\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable,\n" +
                "\t\ttd,\n" +
                "\t\ttr {\n" +
                "\t\t\tvertical-align: top;\n" +
                "\t\t\tborder-collapse: collapse;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t* {\n" +
                "\t\t\tline-height: inherit;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ta[x-apple-data-detectors=true] {\n" +
                "\t\t\tcolor: inherit !important;\n" +
                "\t\t\ttext-decoration: none !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "<style id=\"media-query\" type=\"text/css\">\n" +
                "\t\t@media (max-width: 660px) {\n" +
                "\n" +
                "\t\t\t.block-grid,\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\tmin-width: 320px !important;\n" +
                "\t\t\t\tmax-width: 100% !important;\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.block-grid {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col_cont {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\timg.fullwidth,\n" +
                "\t\t\timg.fullwidthOnMobile {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col {\n" +
                "\t\t\t\tmin-width: 0 !important;\n" +
                "\t\t\t\tdisplay: table-cell !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack.two-up .col {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num2 {\n" +
                "\t\t\t\twidth: 16.6% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num3 {\n" +
                "\t\t\t\twidth: 25% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num4 {\n" +
                "\t\t\t\twidth: 33% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num5 {\n" +
                "\t\t\t\twidth: 41.6% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num6 {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num7 {\n" +
                "\t\t\t\twidth: 58.3% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num8 {\n" +
                "\t\t\t\twidth: 66.6% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num9 {\n" +
                "\t\t\t\twidth: 75% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num10 {\n" +
                "\t\t\t\twidth: 83.3% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.video-block {\n" +
                "\t\t\t\tmax-width: none !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mobile_hide {\n" +
                "\t\t\t\tmin-height: 0px;\n" +
                "\t\t\t\tmax-height: 0px;\n" +
                "\t\t\t\tmax-width: 0px;\n" +
                "\t\t\t\tdisplay: none;\n" +
                "\t\t\t\toverflow: hidden;\n" +
                "\t\t\t\tfont-size: 0px;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.desktop_hide {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t\tmax-height: none !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.img-container.big img {\n" +
                "\t\t\t\twidth: auto !important;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "<style id=\"icon-media-query\" type=\"text/css\">\n" +
                "\t\t@media (max-width: 660px) {\n" +
                "\t\t\t.icons-inner {\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.icons-inner td {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #f8f8f9;\">\n" +
                "<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
                "<table bgcolor=\"#f8f8f9\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #f8f8f9; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#f8f8f9\"><![endif]-->\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #f22e2e;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#f22e2e;\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#f22e2e\"><![endif]-->\n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#f22e2e;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;\"><![endif]-->\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n" +
                "<div class=\"col_cont\" style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><a href=\"www.example.com\" style=\"outline:none\" tabindex=\"-1\" target=\"_blank\"><img align=\"center\" alt=\"Your logo.\" border=\"0\" class=\"center fixedwidth\" src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/BeeFree/beefree-u35koukhrs/logoo.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 160px; max-width: 100%; display: block;\" title=\"Your logo.\" width=\"160\"></a>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #f22e2e;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#f22e2e;\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#f22e2e\"><![endif]-->\n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#f22e2e;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n" +
                "<div class=\"col_cont\" style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 10px; padding-bottom: 10px; font-family: Tahoma, sans-serif\"><![endif]-->\n" +
                "<div style=\"color:#555555;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.5;padding-top:10px;padding-right:40px;padding-bottom:10px;padding-left:40px;\">\n" +
                "<div class=\"txtTinyMce-wrapper\" style=\"font-size: 12px; line-height: 1.5; color: #555555; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 18px;\">\n" +
                "<p style=\"margin: 0; font-size: 12px; text-align: center; line-height: 1.5; word-break: break-word; mso-line-height-alt: 18px; margin-top: 0; margin-bottom: 0;\">&nbsp;</p>\n" +
                "<p style=\"margin: 0; font-size: 18px; text-align: center; line-height: 1.5; word-break: break-word; mso-line-height-alt: 27px; margin-top: 0; margin-bottom: 0;\"><span style=\"font-size: 18px;\"><strong><span style=\"color: #ffffff;\">Order Reference : " + orderRef + "</span></strong></span></p>\n" +
                "<p style=\"margin: 0; font-size: 12px; text-align: center; line-height: 1.5; word-break: break-word; mso-line-height-alt: 18px; margin-top: 0; margin-bottom: 0;\">&nbsp;</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<div style=\"font-size:16px;text-align:center;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif\">\n" +
                "<style>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t.tbl {\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\ttext-align: left;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tpadding: 8px;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tborder: 1px solid #ddd;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tcolor: white;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\"\n" +
                "\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</style>\n" +
                "<div style=\"overflow-x:auto;\">\n" +
                "<table style=\" border-collapse: collapse; border-spacing: 0;width: 90%;margin: 5%\">\n" +
                "<tbody><tr>\n" +
                "<th class=\"tbl\">Item Name</th>\n" +
                "<th class=\"tbl\">Unit Price</th>\n" +
                "<th class=\"tbl\">Quantity</th>\n" +
                "<th class=\"tbl\">Sub Total</th>\n" +
                "</tr>\n" +
                "<tr>\n";

                for (OrderDetailDto dto : commonOrderDTO.getOrderDetails()) {
                    html += "<td class=\"tbl\">" + productRepository.findProductById(dto.getFkProductId()).getName() + "</td>\n" +
                            "<td class=\"tbl\">" + decimalFormat.format(dto.getPrice()) + "</td>\n" +
                            "<td class=\"tbl\">" + dto.getQty() + "</td>\n" +
                            "<td class=\"tbl\">" + decimalFormat.format(dto.getSubTotal()) + "</td>\n" +
                            "</tr>\n" +
                            "<tr>\n";
                }

                html += "<td></td>\n" +
                "<td></td>\n" +
                "<td class=\"tbl\" style=\"color:black;background-color:white\">Discount</td>\n" +
                "<td class=\"tbl\" style=\"color:black ;background-color:white\">0.00</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td></td>\n" +
                "<td></td>\n" +
                "<td class=\"tbl\" style=\"color:black;background-color:white\">Total</td>\n" +
                "<td class=\"tbl\" style=\"color:black ;background-color:white\">" + decimalFormat.format(commonOrderDTO.getTotal()) + "</td>\n" +
                "</tr>\n" +
                "</tbody></table>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #f22e2e;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#f22e2e;\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#f22e2e\"><![endif]-->\n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#f22e2e;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n" +
                "<div class=\"col_cont\" style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: Tahoma, sans-serif\"><![endif]-->\n" +
                "<div style=\"color:#555555;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<div class=\"txtTinyMce-wrapper\" style=\"font-size: 12px; line-height: 1.2; color: #555555; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 14px;\">\n" +
                "<p style=\"margin: 0; font-size: 24px; text-align: center; line-height: 1.2; word-break: break-word; mso-line-height-alt: 29px; margin-top: 0; margin-bottom: 0;\"><span style=\"font-size: 24px;\"><strong><span style=\"color: #ffffff;\">Thank You !</span></strong></span></p>\n" +
                "<p style=\"margin: 0; font-size: 12px; text-align: center; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\">&nbsp;</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<div style=\"font-size:16px;text-align:center;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif\"></div>\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" class=\"social_icons\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
                "<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" class=\"social_table\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-tspace: 0; mso-table-rspace: 0; mso-table-bspace: 0; mso-table-lspace: 0;\" valign=\"top\">\n" +
                "<tbody>\n" +
                "<tr align=\"center\" style=\"vertical-align: top; display: inline-block; text-align: center;\" valign=\"top\">\n" +
                "\t<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.facebook.com/\" target=\"_blank\"><img alt=\"Facebook\" height=\"32\" src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/BeeFree/beefree-u35koukhrs/icons8-facebook-64.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\" title=\"Facebook\" width=\"32\"></a></td>\n" +
                "\t<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://twitter.com/\" target=\"_blank\"><img alt=\"Twitter\" height=\"25\" src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/BeeFree/beefree-u35koukhrs/icons8-twitter-48.png\" style=\"width:60%;text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\" title=\"Twitter\" width=\"32\"></a></td>\n" +
                "\t<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://instagram.com/\" target=\"_blank\"><img alt=\"Instagram\" height=\"25\" src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/BeeFree/beefree-u35koukhrs/icons8-instagram-52.png\" style=\"width:50%;text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\" title=\"Instagram\" width=\"32\"></a></td>\n" +
                "\t<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.youtube.com/\" target=\"_blank\"><img alt=\"youtube\" height=\"32\" src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/BeeFree/beefree-u35koukhrs/icons8-youtube-64.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\" title=\"LinkedIn\" width=\"32\"></a></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 20px; padding-bottom: 30px; font-family: Tahoma, sans-serif\"><![endif]-->\n" +
                "<div style=\"color:#555555;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:20px;padding-right:40px;padding-bottom:30px;padding-left:40px;\">\n" +
                "<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; color: #555555; mso-line-height-alt: 14px;\">\n" +
                "<p style=\"margin: 0; text-align: center; font-size: 12px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ffffff; font-size: 12px;\">Contact</span></p>\n" +
                "<p style=\"margin: 0; text-align: center; font-size: 12px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ffffff; font-size: 12px;\">1912 &nbsp;Mcwhorter Road, FL 11223</span></p>\n" +
                "<p style=\"margin: 0; text-align: center; font-size: 12px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ffffff; font-size: 12px;\">+111 222 333 | Info@chillicompany.com</span></p>\n" +
                "<p style=\"margin: 0; text-align: center; font-size: 12px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\">&nbsp;</p>\n" +
                "<p style=\"margin: 0; text-align: center; font-size: 12px; line-height: 1.2; word-break: break-word; mso-line-height-alt: 14px; margin-top: 0; margin-bottom: 0;\">&nbsp;</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #f22e2e;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#f22e2e;\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#f22e2e\"><![endif]-->\n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#f22e2e;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;\"><![endif]-->\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n" +
                "<div class=\"col_cont\" style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody><tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td align=\"center\" style=\"word-break: break-word; vertical-align: top; padding-top: 5px; padding-right: 0px; padding-bottom: 5px; padding-left: 0px; text-align: center;\" valign=\"top\">\n" +
                "<!--[if vml]><table align=\"left\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;\"><![endif]-->\n" +
                "<!--[if !vml]><!-->\n" +
                "\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "\n" +
                "<!--<![endif]-->\n" +
                "\n" +
                "\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "\n" +
                "\n" +
                "\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (IE)]></div><![endif]-->\n" +
                "\n" +
                "</div></div></div></div></div></div></td></tr></tbody></table></body></html>";

        User user = userRepository.findUserById(commonOrderDTO.getFkUserId());
        mailSender.sendEmail(user.getEmail(), doEmail+",", ORDER_PLACE_EMAIL_SUBJECT, html, true, null);
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getAllTopOrders(int yr, int mth) {
        try {
            log.info("Execute getAllTopOrders: ");
            List<Order> top10OrdersByYearAndMonth = orderRepository.getTop10OrdersByYearAndMonth(yr, mth);
            List<FullOrderDTO> orderList = new ArrayList<>();
            for (Order r : top10OrdersByYearAndMonth) {
                FullOrderDTO fullOrderDTO = new FullOrderDTO();
                fullOrderDTO.setId(r.getId());
                fullOrderDTO.setTotal(r.getTotal());
                fullOrderDTO.setMessage(r.getMessage());
                fullOrderDTO.setOrderRef(r.getOrderRef());
                fullOrderDTO.setStatus(r.getStatus());
                fullOrderDTO.setCreateDate(r.getCreateDate());
                UserDto userDTO = modelMapper.map(r.getFkUser(), UserDto.class);
                userDTO.setPassword(null);
                fullOrderDTO.setFkUser(userDTO);

                List<FullOrderDetailsDTO> orderdtolist =  new ArrayList<>();
                for (OrderDetail order : r.getFkOrder()) {

                    FullOrderDetailsDTO fullOrderDetailsDTO = new FullOrderDetailsDTO();
                    fullOrderDetailsDTO.setId(order.getId());
                    fullOrderDetailsDTO.setPrice(order.getPrice());
                    fullOrderDetailsDTO.setSubTotal(order.getSubTotal());
                    fullOrderDetailsDTO.setQty(order.getQty());

                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(order.getFkProduct().getId());
                    productDTO.setCode(order.getFkProduct().getCode());
                    productDTO.setName(order.getFkProduct().getName());
                    productDTO.setThumbnail(order.getFkProduct().getThumbnail());
                    productDTO.setPrice(order.getFkProduct().getPrice());
                    productDTO.setStatus(order.getFkProduct().getStatus());
                    productDTO.setTotalQty(order.getFkProduct().getTotalQty());
                    productDTO.setCurrentQty(order.getFkProduct().getCurrentQty());
                    productDTO.setCreateDate(order.getFkProduct().getCreateDate());
                    productDTO.setCategories(new ArrayList<>());


                    fullOrderDetailsDTO.setProduct(productDTO);

                    orderdtolist.add(fullOrderDetailsDTO);

                }

                fullOrderDTO.setFkOrder(orderdtolist);
                orderList.add(fullOrderDTO);
            }

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, orderList), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Execute getAllTopOrders: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getAllCustomerOrdersById(int customerId) {
        log.info("Execute getAllCustomerOrdersById: customerId :" + customerId);
        try {
            Optional<User> userOptional = userRepository.findById(customerId);
            if(!userOptional.isPresent()) throw new SwlcExportMarketException(404, "User not found");
            List<Order> top10OrdersByYearAndMonth = orderRepository.getOrdersByUserId(userOptional.get().getId());

            List<FullOrderDTO> fullOrderDTOList =  new ArrayList<>();

            for (Order r : top10OrdersByYearAndMonth) {
                FullOrderDTO fullOrderDTO = new FullOrderDTO();
                fullOrderDTO.setId(r.getId());
                fullOrderDTO.setTotal(r.getTotal());
                fullOrderDTO.setMessage(r.getMessage());
                fullOrderDTO.setOrderRef(r.getOrderRef());
                fullOrderDTO.setStatus(r.getStatus());
                fullOrderDTO.setCreateDate(r.getCreateDate());
                UserDto userDTO = modelMapper.map(r.getFkUser(), UserDto.class);
                userDTO.setPassword(null);
                fullOrderDTO.setFkUser(userDTO);

                List<FullOrderDetailsDTO> orderdtolist =  new ArrayList<>();
                for (OrderDetail order : r.getFkOrder()) {

                    FullOrderDetailsDTO fullOrderDetailsDTO = new FullOrderDetailsDTO();
                    fullOrderDetailsDTO.setId(order.getId());
                    fullOrderDetailsDTO.setPrice(order.getPrice());
                    fullOrderDetailsDTO.setSubTotal(order.getSubTotal());
                    fullOrderDetailsDTO.setQty(order.getQty());

                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(order.getFkProduct().getId());
                    productDTO.setCode(order.getFkProduct().getCode());
                    productDTO.setName(order.getFkProduct().getName());
                    productDTO.setThumbnail(order.getFkProduct().getThumbnail());
                    productDTO.setPrice(order.getFkProduct().getPrice());
                    productDTO.setStatus(order.getFkProduct().getStatus());
                    productDTO.setTotalQty(order.getFkProduct().getTotalQty());
                    productDTO.setCurrentQty(order.getFkProduct().getCurrentQty());
                    productDTO.setCreateDate(order.getFkProduct().getCreateDate());
                    productDTO.setCategories(new ArrayList<>());


                    fullOrderDetailsDTO.setProduct(productDTO);

                    orderdtolist.add(fullOrderDetailsDTO);

                }

                fullOrderDTO.setFkOrder(orderdtolist);
                fullOrderDTOList.add(fullOrderDTO);
            }

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, fullOrderDTOList), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Execute getAllCustomerOrdersById: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<CommonResponseDTO> getAllOrdersByAdmin(String ref) {
        log.info("Execute getAllOrdersByAdmin: ");
        try {
            List<Order> top10OrdersByYearAndMonth = orderRepository.getAllOrdersWithRef(ref);

            List<FullOrderDTO> fullOrderDTOList =  new ArrayList<>();

            for (Order r : top10OrdersByYearAndMonth) {
                FullOrderDTO fullOrderDTO = new FullOrderDTO();
                fullOrderDTO.setId(r.getId());
                fullOrderDTO.setTotal(r.getTotal());
                fullOrderDTO.setMessage(r.getMessage());
                fullOrderDTO.setOrderRef(r.getOrderRef());
                fullOrderDTO.setStatus(r.getStatus());
                fullOrderDTO.setCreateDate(r.getCreateDate());
                UserDto userDTO = modelMapper.map(r.getFkUser(), UserDto.class);
                userDTO.setPassword(null);
                fullOrderDTO.setFkUser(userDTO);

                List<FullOrderDetailsDTO> orderdtolist =  new ArrayList<>();
                for (OrderDetail order : r.getFkOrder()) {

                    FullOrderDetailsDTO fullOrderDetailsDTO = new FullOrderDetailsDTO();
                    fullOrderDetailsDTO.setId(order.getId());
                    fullOrderDetailsDTO.setPrice(order.getPrice());
                    fullOrderDetailsDTO.setSubTotal(order.getSubTotal());
                    fullOrderDetailsDTO.setQty(order.getQty());

                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(order.getFkProduct().getId());
                    productDTO.setCode(order.getFkProduct().getCode());
                    productDTO.setName(order.getFkProduct().getName());
                    productDTO.setThumbnail(order.getFkProduct().getThumbnail());
                    productDTO.setPrice(order.getFkProduct().getPrice());
                    productDTO.setStatus(order.getFkProduct().getStatus());
                    productDTO.setTotalQty(order.getFkProduct().getTotalQty());
                    productDTO.setCurrentQty(order.getFkProduct().getCurrentQty());
                    productDTO.setCreateDate(order.getFkProduct().getCreateDate());

                    List<ProductCategory> fkProductCategories = order.getFkProduct().getFkProductCategories();
                    List<CategoryDTO> categoryDTOList = new ArrayList<>();
                    for (ProductCategory p : fkProductCategories) {
                        categoryDTOList.add(modelMapper.map(p, CategoryDTO.class));
                    }

                    productDTO.setCategories(categoryDTOList);

                    fullOrderDetailsDTO.setProduct(productDTO);

                    orderdtolist.add(fullOrderDetailsDTO);

                }

                fullOrderDTO.setFkOrder(orderdtolist);
                fullOrderDTOList.add(fullOrderDTO);
            }

            return new ResponseEntity<>(new CommonResponseDTO(true, REQUEST_SUCCESS_MESSAGE, fullOrderDTOList), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Execute getAllOrdersByAdmin: " + e.getMessage(), e);
            throw e;
        }
    }

}
