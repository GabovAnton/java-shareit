package ru.practicum.shareit.request;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RequestWithProposalsDto> getAll(Long userId) {
        QRequest request = QRequest.request;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory.selectFrom(request)
                .where(request.requester.id.eq(userId))
                .orderBy(request.createdDate.desc())
                .fetch().stream() //TODO verify!!!!
                .filter(Objects::nonNull)
                .map(requestMapper::requestToRequestWithProposalDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public RequestDto SaveRequest(RequestDto requestDto, Long userId) {
        Request request = requestMapper.requestDtoToRequest(requestDto);
        request.setRequester(userService.getUser(userId));
        Request save = requestRepository.save(request);

        return requestMapper.requestToRequestDto(save);
    }

    @Override
    public List<RequestDto> getAllFromOthers(int from, int size, Long userId) {
        // int page = (int) Math.round(Math.floor(from / size));

        //  PageRequest pageRequest = PageRequest.of(page, size, Sort.by("created_date"));
        if (from < 1) {
            //EXCEPTION!!!
        }

        QRequest request = QRequest.request;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(request)
                .where(request.requester.id.notIn(userId))
                 .orderBy(request.createdDate.desc())
                 .limit(size)
                .offset(--from)
                .fetch().stream() //TODO verify!!!!
                .map(requestMapper::requestToRequestDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        /*      return   requestRepository.getByRequester_IdNotOrderByCreatedDateDesc(userId, pageRequest).stream()
                .filter(Objects::nonNull)
                .map(requestMapper::requestToRequestDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));*/
    }

    @Override
    public RequestWithProposalsDto GetRequest(Long requestId, Long userId) {

        QRequest request = QRequest.request;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return requestMapper.requestToRequestWithProposalDto(queryFactory.selectFrom(request)
                .where(request.requester.id.eq(userId))
                .fetchOne());
    }
}
