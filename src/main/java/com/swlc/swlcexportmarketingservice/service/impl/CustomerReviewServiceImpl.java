package com.swlc.swlcexportmarketingservice.service.impl;

import com.swlc.swlcexportmarketingservice.dto.CustomerReviewsDto;
import com.swlc.swlcexportmarketingservice.dto.UserDto;
import com.swlc.swlcexportmarketingservice.entity.CustomerReviews;
import com.swlc.swlcexportmarketingservice.entity.User;
import com.swlc.swlcexportmarketingservice.exception.SwlcExportMarketException;
import com.swlc.swlcexportmarketingservice.repository.CustomerReviewRepository;
import com.swlc.swlcexportmarketingservice.repository.UserRepository;
import com.swlc.swlcexportmarketingservice.service.CustomerReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

@Service
public class CustomerReviewServiceImpl implements CustomerReviewService {

    private final UserRepository userRepository;
    private final CustomerReviewRepository customerReviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerReviewServiceImpl(UserRepository userRepository, CustomerReviewRepository customerReviewRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.customerReviewRepository = customerReviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveNewReview(CustomerReviewsDto dto) {
        User userEntity = null;
        try {
            UserDto user = dto.getUser();
            if (user != null) {
                userEntity = userRepository.findUserById(user.getId());
                if (userEntity == null) throw new SwlcExportMarketException(404, "User not found");
            }
            customerReviewRepository.save(
                    new CustomerReviews(
                            dto.getCustomerReviewType(),
                            dto.getCustomerReviewComment(),
                            new Date(),
                            userEntity
                    )
            );

            return true;

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<CustomerReviewsDto> getCustomerReviews(int year, int month, Pageable pageable) {
        String startDate = null;
        String endDate = null;
        Date sDate = null;
        Date eDate = null;
        try {
            if (year > 0) {
                if (month > 0) {
                    YearMonth yearMonthObject = YearMonth.of(year, month);
                    int daysInMonth = yearMonthObject.lengthOfMonth(); //28
                    startDate = year + "-" + month + "-1";
                    endDate = year + "-" + month + "-" + daysInMonth;
                } else {
                    startDate = year + "-1-1";
                    endDate = year + "-12-31";
                }
                sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            }

            Page<CustomerReviewsDto> customerReviews = customerReviewRepository.filterCustomerReviewsByDate(sDate, eDate, pageable).map(this::getCustomerReviewsDto);

            return customerReviews;

        } catch (ParseException ex) {
            throw new SwlcExportMarketException(201, "");
        } catch (Exception e) {
            throw e;
        }

    }


    private CustomerReviewsDto getCustomerReviewsDto (CustomerReviews cr){
        return modelMapper.map(cr, CustomerReviewsDto.class);
    }

}
