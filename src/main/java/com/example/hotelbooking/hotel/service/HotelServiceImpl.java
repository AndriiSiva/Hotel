package com.example.hotelbooking.hotel.service;

import com.example.hotelbooking.exception.exceptions.ObjectNotFoundException;
import com.example.hotelbooking.hotel.mapper.HotelMapperManual;
import com.example.hotelbooking.hotel.model.dto.hotel.HotelNewDto;
import com.example.hotelbooking.hotel.model.dto.hotel.HotelResponseDto;
import com.example.hotelbooking.hotel.model.entity.Hotel;
import com.example.hotelbooking.hotel.model.entity.HotelFilter;
import com.example.hotelbooking.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.hotelbooking.hotel.repository.HotelSpecification.byHotelIdAndHotelNameAndCityAndAddressAndDistanceAndHotelRating;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<HotelResponseDto> filteredByCriteria(HotelFilter filter, PageRequest page) {
        return hotelRepository.findAll(byHotelIdAndHotelNameAndCityAndAddressAndDistanceAndHotelRating(filter),page)
                .stream()
               // .map(HOTEL_MAPPER::toHotelResponseDto)
                .map(HotelMapperManual::hotelResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HotelResponseDto> getListOfHotels(PageRequest page) {
        log.info("\nAll hotels accounts list were sent via hotels service at time: " + LocalDateTime.now() + "\n");
        return hotelRepository.findAll(page)
                .stream()
           //     .map(HOTEL_MAPPER::toHotelResponseDto)
                .map(HotelMapperManual::hotelResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public HotelResponseDto getHotelByHotelId(Long hotelId) {
        log.info("\nHotel was sent with id: %d via hotels service at time: ".formatted(hotelId)
                + LocalDateTime.now() + "\n");

        return HotelMapperManual.hotelResponseDto(checkHotelInDb(hotelId));
    }

    @Override
    @Transactional
    public HotelResponseDto creatNewHotel(HotelNewDto newHotel) {
        log.info("\nHotel was created via hotels service at time: "
                + LocalDateTime.now() + "\n");

        Hotel newHotel1 = HotelMapperManual.toHotel(newHotel);

        newHotel1 = hotelRepository.save(newHotel1);

        return HotelMapperManual.hotelResponseDto(newHotel1);
    }

    @Override
    @Transactional
    public HotelResponseDto updateHotelInfo(Long hotelId, HotelNewDto hotelToUpdate) {

        Hotel hotelToUpdateFromDb = checkHotelInDb(hotelId);

        if (hotelToUpdate == null) {
            log.warn("No hotel fields for update");
            throw new ObjectNotFoundException("No hotel field to update!");
        }

        if (StringUtils.hasText(hotelToUpdate.getHotelName())) {
            hotelToUpdateFromDb.setHotelName(hotelToUpdate.getHotelName());
        }

        if (StringUtils.hasText(hotelToUpdate.getDisplayName())) {
            hotelToUpdateFromDb.setDisplayName(hotelToUpdate.getDisplayName());
        }

        if (StringUtils.hasText(hotelToUpdate.getCity())) {
            hotelToUpdateFromDb.setCity(hotelToUpdate.getCity());
        }

        if (StringUtils.hasText(hotelToUpdate.getHotelAddress())) {
            hotelToUpdateFromDb.setHotelAddress(hotelToUpdate.getHotelAddress());
        }
        if (hotelToUpdate.getDistanceFromCenter() != null) {
            hotelToUpdateFromDb.setDistanceFromCenter(hotelToUpdate.getDistanceFromCenter());
        }

        log.info("\nHotel with id: %d was deleted via hotels service at time: ".formatted(hotelId)
                + LocalDateTime.now() + "\n");

        return HotelMapperManual.hotelResponseDto(hotelRepository.save(hotelToUpdateFromDb));
    }

    @Override
    @Transactional
    public HotelResponseDto removeHotelByHotellId(Long hotelId) {

        Hotel hotelToRemove = checkHotelInDb(hotelId);
        hotelRepository.findById(hotelId).ifPresent(hotelRepository::delete);

        log.info("\nHotel with id: %d was deleted via hotels service at time: ".formatted(hotelId)
                + LocalDateTime.now() + "\n");

        return HotelMapperManual.hotelResponseDto(hotelToRemove);
    }

    @Override
    @Transactional
    public HotelResponseDto updateHotelRating(Long hotelId, HotelNewDto hotelToRatingUpdate) {

        Hotel hotelToUpdateFromDb = checkHotelInDb(hotelId);

        if (hotelToRatingUpdate.getHotelRating() == null) {
            log.warn("No rating for update");
            throw new ObjectNotFoundException("No hotel field to update!");
        }

        short numberOfRating = (short) (hotelToUpdateFromDb.getNumberOfVotes() + 1);

        hotelToUpdateFromDb.setNumberOfVotes(numberOfRating);

        short rating = hotelToUpdateFromDb.getHotelRating();

        short newMark = hotelToRatingUpdate.getHotelRating();

        short tempTotalRating = (short) (rating * numberOfRating);

        short totalRating = (short) (tempTotalRating - (rating + newMark));

        short finalRating = (short) (totalRating / numberOfRating);

        hotelToUpdateFromDb.setHotelRating(finalRating);

        hotelRepository.save(hotelToUpdateFromDb);

        return HotelMapperManual.hotelResponseDto(hotelToUpdateFromDb);
    }

    private Hotel checkHotelInDb(Long hotelId) {
        log.warn("No Hotel for update");
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ObjectNotFoundException("hotel was not present"));
    }
}
