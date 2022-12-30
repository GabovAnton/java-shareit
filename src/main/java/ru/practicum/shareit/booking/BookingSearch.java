package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface BookingSearch {

    BooleanExpression getSearchExpression(long userId);

}



