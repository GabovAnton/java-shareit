package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("")
    public List<RequestWithProposalsDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAll(userId);

    }

    @PostMapping()
    public RequestDto create(@Valid @RequestBody RequestDto requestDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.SaveRequest(requestDto, userId);
    }

    //GET /requests/all?from={from}&size={size}
    @PostMapping("/requests/all?from={from}&size={size}")
    public List<RequestDto> getAllFromOthers(@PathVariable Integer from, @PathVariable Integer size,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllFromOthers(size, from, userId);
    }

    //GET /requests/{requestId}
    @GetMapping("{requestId}")
    public RequestWithProposalsDto getBookingById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.GetRequest(requestId, userId);
    }
}
