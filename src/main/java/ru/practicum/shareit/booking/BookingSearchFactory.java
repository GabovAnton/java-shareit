package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookingSearchFactory {

    static Map<String, BookingSearch> searchMap = new HashMap<>();

    static  {
        searchMap.put("ALL", new All());
        searchMap.put("CURRENT", new Current());
        searchMap.put("PAST", new Past());
        searchMap.put("FUTURE", new Future());
        searchMap.put("WAITING", new Waiting());
        searchMap.put("REJECTED", new Rejected());
    }



    public static Optional<BookingSearch> getSearchMethod(String operator) {
        return Optional.ofNullable(searchMap.get(operator));
    }
}
