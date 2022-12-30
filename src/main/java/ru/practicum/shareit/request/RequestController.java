package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestService requestService;

    @GetMapping("")
    public List<RequestWithProposalsDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAll(userId);

    }

    @PostMapping()
    public RequestDto create(@Valid @RequestBody RequestDto requestDto,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.saveRequest(requestDto, userId);
    }

    @GetMapping("/all")
    public List<RequestWithProposalsDto> getAllFromOthers(
            @RequestParam(required = false) @Min(value = 0, message = "from should be positive") Integer from,
            @RequestParam(required = false) @Min(value = 0, message = "size should greater than 1") Integer size,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getAllFromOthers(from, size, userId);
    }

    @GetMapping("{requestId}")
    public RequestWithProposalsDto getBookingById(@PathVariable Long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequest(requestId, userId);
    }
}
