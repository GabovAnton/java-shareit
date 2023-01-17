package ru.practicum.shareit.gateway.user.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.practicum.shareit.gateway.exception.BadRequestException;
import ru.practicum.shareit.gateway.exception.EntityNotFoundException;
import ru.practicum.shareit.gateway.exception.ForbiddenException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {

        switch (response.status()) {
            case 400:
                return new ForbiddenException("Bad Request error from server");
            case 404:
                return new EntityNotFoundException("Not found error from server");
            case 403:
                return new BadRequestException("Forbidden error from server");
            default:
                return new Exception("Generic exception from server");
        }
    }

}
