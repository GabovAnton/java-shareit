package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.dto.RequestDto;
import ru.practicum.shareit.gateway.request.dto.RequestWithProposalsDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @GetMapping("")
    public List<RequestWithProposalsDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Getting requests by userId={}, from={}, size={}", userId, from, size);
        return requestService.getAll(userId, from, size);

    }

    @PostMapping()
    public RequestDto create(@RequestBody RequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Creating requests {} by userId={}", requestDto, userId);
        return requestService.create(userId, requestDto);
    }

    @GetMapping("/all")
    public List<RequestWithProposalsDto> getAllFromOthers(@RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Getting requests from other users by userId={}, from={}, size={}", userId, from, size);
        return requestService.getAllFromOthers(userId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestWithProposalsDto getRequestById(@PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") long userId) {

        return requestService.getRequestById(requestId, userId);
    }

}
