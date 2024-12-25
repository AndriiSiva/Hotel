package com.example.hotelbooking.hotel.repository;

import com.example.hotelbooking.hotel.model.entity.Hotel;
import com.example.hotelbooking.hotel.model.entity.HotelFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {

    static Specification<Hotel> byHotelIdAndHotelNameAndCityAndAddressAndDistanceAndHotelRating(HotelFilter  filter) {

        return Specification.where(byHotelId(filter.getId()))
                .and(byHotelName(filter.getHotelName()))
                .and(byHotelDisplayName(filter.getDisplayName()))
                .and(byCity(filter.getCity()))
                .and(byHotelAddress(filter.getHotelAddress()))
                .and(byDistanceFromCenter(filter.getDistanceFromCenter()))
                .and(byHotelRatingAndNumberOfVotes(filter.getHotelRating(), filter.getNumberOfVotes()));
    }

    static Specification<Hotel> byHotelId(Long hotelId) {

        return (root, query, cb) -> {

            if (hotelId == null) {
                return null;
            }

            return cb.equal(root.get("id"), hotelId);
        };
    }

    static Specification<Hotel> byHotelName(String hotelName) {
        return (root, query, cb) -> {

            if (hotelName == null) {
                return null;
            }

            return cb.equal(root.get("hotelName"), hotelName);
        };
    }

    static Specification<Hotel> byHotelDisplayName(String hotelDisplayName) {

        return (root, query, cb) -> {

            if (hotelDisplayName == null) {
                return null;
            }

            return cb.equal(root.get("displayName"), hotelDisplayName);
        };
    }

    static Specification<Hotel> byCity(String city) {

        return (root, query, cb) -> {

            if (city == null) {
                return null;
            }

            return cb.equal(root.get("city"), city);
        };
    }

    static Specification<Hotel> byHotelAddress(String hotelAddress) {

        return (root, query, cb) -> {

            if (hotelAddress == null) {
                return null;
            }

            return cb.equal(root.get("hotelAddress"), hotelAddress);
        };
    }

    static Specification<Hotel> byDistanceFromCenter(Long distanceFromCenter) {

        return (root, query, cb) -> {

            if (distanceFromCenter == null) {
                return null;
            }

            return cb.equal(root.get("distanceFromCenter"), distanceFromCenter);
        };
    }

    static Specification<Hotel> byHotelRatingAndNumberOfVotes(Short hotelRating, Short numberOfVotes) {

        return (root, query, cb) -> {

            if (hotelRating == null) {
                return null;
            }

            Predicate hotelRatings =  cb.equal(root.get("hotelRating"), hotelRating);
            Predicate numbersOfVotes = cb.equal(root.get("numberOfVotes"), numberOfVotes);

            if (numberOfVotes == null) {
                return cb.equal(hotelRatings, hotelRating);
            }

            return cb.and(hotelRatings, numbersOfVotes);
        };
    }
}

