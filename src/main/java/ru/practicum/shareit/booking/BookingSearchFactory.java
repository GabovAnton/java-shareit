package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Component
public class BookingSearchFactory {
    private SearchAll searchAll = new SearchAll();
    private SearchRejected searchRejected = new SearchRejected();
    private SearchPast searchPast = new SearchPast();
    private SearchFuture searchFuture = new SearchFuture();
    private SearchCurrent searchCurrent = new SearchCurrent();
    private SearchWaiting searchWaiting = new SearchWaiting();

    Map<String, BookingSearch> searchMap = new HashMap<>() {{
        put("ALL", searchAll);
        put("CURRENT", searchCurrent);
        put("PAST", searchPast);
        put("FUTURE", searchFuture);
        put("WAITING", searchWaiting);
        put("REJECTED", searchRejected);
    }};


    public Optional<BookingSearch> getSearchMethod(String operator) {
        return Optional.ofNullable(searchMap.get(operator));
    }
}
