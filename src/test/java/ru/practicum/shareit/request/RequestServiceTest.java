package ru.practicum.shareit.request;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemLastBookingDto;
import ru.practicum.shareit.item.ItemNextBookingDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private final EntityManager em;
    @Mock
    CriteriaBuilder builder;
    @Mock
    CriteriaQuery<Request> criteriaQuery;
    @Mock
    CriteriaQuery<Long> countCriteriaQuery;
    @Mock
    TypedQuery<Request> query;
    LocalDateTime currentDate = LocalDateTime
            .of(2022, 12, 10, 5, 5, 5, 5);
    @Mock
    private RequestMapper requestMapper;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JPAQueryFactory queryFactory;
    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private RequestService requestService =
            new RequestServiceImpl(
                    requestRepository,
                    userService,
                    requestMapper

            );

    @Test
    void getAll_ByWrongUserShouldThrowException() {

        ReflectionTestUtils.setField(requestService, "userService", userService);

        Long userId = 100L;
        when(userService.getUser(anyLong()))
                .thenThrow(new EntityNotFoundException("user with id: " + userId + " doesn't exists"));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            requestService.getAll(100L);
        });
        assertThat(entityNotFoundException.getMessage(),
                equalTo("user with id: " + userId + " doesn't exists"));

    }

    @Test
    void getAll_ShouldReturnList() {

     /*   ReflectionTestUtils.setField(requestService, "entityManager", em);


        ReflectionTestUtils.setField(requestService, "userService", userService);
        RequestWithProposalsDto requestWithProposalsDto = makeRequestWithProposalsDto();
        List<RequestWithProposalsDto> expectedItems = List.of(requestWithProposalsDto);
        User user = makeUser();

        Long userId =100L;

      *//*  when(entityManagerFactory.getCache())
                .thenReturn(emfCache);*//*
        when(em.getEntityManagerFactory())
                .thenReturn(entityManagerFactory);
        when(em.getEntityManagerFactory())
                .thenReturn(entityManagerFactory);
       *//* when(entityManagerMock.createNamedQuery("User.findAll"))
                .thenReturn(findAllQuery);
        when(findAllQuery.getResultList())
                .thenReturn(mockUsersDbCollection);*//*


     //   when(em.find(any(),anyLong())).thenReturn( makeRequest());
        //when(em.)
       // when(em.find(Customer.class,1L)).thenReturn(sampleCustomer);
       // JPAQuery jpaQuery = Mockito.mock(JPAQuery.class<>(em));

        //QRequest qRequest   = Mockito.mock(QRequest.class);

       // ReflectionTestUtils.setField(requestService, "query", jpaQuery);

      //  when(jpaQuery.fetch()).thenReturn(expectedItems);
//when(jpaQuery.from(qRequest)).thenReturn(jpaQuery);

*//*
        when(queryFactory.selectFrom(any())).thenReturn(jpaQuery);
     //   when(queryFactory.selectFrom(any())).thenReturn(jpaQuery);
        when(jpaQuery.where(any(BooleanExpression.class))).thenReturn(jpaQuery);
        when(jpaQuery.orderBy(any(OrderSpecifier.class))).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expectedItems);*//*

     //   requestService.getAll(100L);

        *//*when(jpaQuery.select(any(Expression.class))).thenReturn(jpaQuery);

        when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
        when(jpaQuery.offset(anyLong())).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(expectedItems);*//*
        //JPAQueryFactory queryFactory = Mockito.mock(JPAQueryFactory.class, Mockito.RETURNS_DEEP_STUBS);



        when(userService.getUser(anyLong()))
                .thenReturn(makeUser());

    //    JPAQuery step1 = Mockito.mock(JPAQuery.class);


       *//* when(queryFactory.from(qRequest)).thenReturn(step1);


        JPAQuery step2 = Mockito.mock(JPAQuery.class);
        Mockito.when(step1.where(any(BooleanExpression.class))).thenReturn(step2);

        JPAQuery step3 = Mockito.mock(JPAQuery.class);
        Mockito.when(step2.orderBy(any(OrderSpecifier.class))).thenReturn(step3);

        when(step2.fetch()).thenReturn(expectedItems);
*//*


        when(em.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(Request.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(makeRequest()));

        //when(em.createQuery(any(), eq(Request.class))).thenReturn(query);
     *//*   when(query.setParameter(any(String.class), any(String.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(makeRequest()));*//*

        requestService.getAll(100L);*/

    }

    @Test
    void save_ShouldNotThrowException() {
        ReflectionTestUtils.setField(requestService, "requestRepository", requestRepository);
        ReflectionTestUtils.setField(requestService, "userService", userService);
        ReflectionTestUtils.setField(requestService, "requestMapper", requestMapper);
        RequestDto requestDto = makeRequestDto();

        when(requestMapper.requestDtoToRequest(requestDto))
                .thenReturn(makeRequest());
        when(userService.getUser(anyLong()))
                .thenReturn(makeUser());

        requestService.saveRequest(requestDto, 100L);

        Mockito.verify(requestRepository).save(requestArgumentCaptor.capture());
        Request savedRequest = requestArgumentCaptor.getValue();

        assertThat(savedRequest.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(savedRequest.getId(), equalTo(requestDto.getId()));

    }

    @Test
    void getAllFromOthers() {


    }

    @Test
    void getRequest() {
    }

    private ItemDto makeItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(100L);
        itemDto.setName("thing");
        itemDto.setDescription("just simple thing");
        itemDto.setAvailable(true);
        itemDto.setRequestId(100L);
        itemDto.setLastBooking(new ItemLastBookingDto(100L, 100L));
        itemDto.setNextBooking(new ItemNextBookingDto(100L, 100L));

        CommentDto commentDto = new CommentDto(100L,
                "good thing",
                101L,
                "Artur",
                LocalDateTime.now().minusDays(100));

        itemDto.setComments(Set.of(commentDto));
        itemDto.setOwner(new UserDto());

        return itemDto;
    }

    private UserDto makeUserDto() {
        UserDto user = new UserDto();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");
        user.setRegistrationDate(currentDate.minusDays(15));
        return user;
    }

    private User makeUser() {
        User user = new User();
        user.setId(100L);
        user.setName("Artur");
        user.setEmail("artur@gmail.com");

        return user;
    }

    private RequestDto makeRequestDto() {
        return new RequestDto(
                100L,
                "simple test description",
                makeUserDto(),
                currentDate.minusDays(1)

        );

    }

    private Request makeRequest() {
        Request request = new Request();
        request.setDescription("simple test description");
        request.setRequester(makeUser());
        request.setCreated(currentDate.minusDays(1));
        request.setId(100L);
        return request;
    }

    private RequestWithProposalsDto makeRequestWithProposalsDto() {
        return new RequestWithProposalsDto(
                100L,
                "some item",
                makeUserDto(),
                currentDate.minusDays(2),
                List.of(makeItemDto())
        );
    }
}