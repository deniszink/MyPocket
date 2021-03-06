package com.denis.data.entity.mapper;

import com.denis.data.ApplicationTestCase;


public class CycleOperationDataMapperTest extends ApplicationTestCase{
    /*private CycleOperationDataMapper dataMapper;

    private static final int FAKE_ID = 34;
    private static final String FAKE_NAME = "gas";
    private static final String FAKE_INTERVAL = CycleOperation.CircleTypes.YEAR.getValue();

    @Mock private TransactionEntity entity;
    @Mock
    private Transaction transaction;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TransactionDataMapper transactionDataMapper;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        dataMapper = new CycleOperationDataMapper(transactionDataMapper);
    }

    @Test
    public void testCycleOperationToCycleOperationEntity(){
        CycleOperation operation = createCycleOperation();
        CycleOperationEntity entity = dataMapper.toEntity(operation);

        assertThat(entity, is(notNullValue()));
        assertThat(entity.getId(),is(FAKE_ID));
        assertThat(entity.getInterval(),is(FAKE_INTERVAL));
        assertThat(entity.getUsername(),is(FAKE_NAME));
        assertThat(entity.getTransactionEntity(),is(notNullValue()));
        assertThat(entity.getTransactionEntity(),is(instanceOf(TransactionEntity.class)));
    }

    @Test
    public void testCycleOperationEntityToCycleOperation(){
        CycleOperationEntity entity = createCycleOperationEntity();
        CycleOperation operation = dataMapper.toEntity(entity);

        assertThat(operation, is(notNullValue()));
        assertThat(operation.getId(),is(FAKE_ID));
        assertThat(operation.getInterval(),is(FAKE_INTERVAL));
        assertThat(operation.getUsername(),is(FAKE_NAME));
        assertThat(operation.getTransaction(),is(notNullValue()));
        assertThat(operation.getTransaction(),is(instanceOf(Transaction.class)));
    }

    @Test
    public void testCycleOperationListToCycleOperationEntityList(){
        CycleOperation operation = mock(CycleOperation.class);
        CycleOperation operation1 = mock(CycleOperation.class);

        List<CycleOperation> list = new ArrayList<>(5);
        list.add(operation);
        list.add(operation1);

        List<CycleOperationEntity> entities = dataMapper.toEntity(list);

        assertThat(entities, is(notNullValue()));
        assertThat(entities.size(), is(2));

        assertThat(entities.get(0), is(instanceOf(CycleOperationEntity.class)));
        assertThat(entities.get(1), is(instanceOf(CycleOperationEntity.class)));

        assertThat(entities.get(0).getTransactionEntity(),is(notNullValue()));
        assertThat(entities.get(1).getTransactionEntity(),is(notNullValue()));


    }

    @Test
    public void testCycleOperationEntityListToCycleOperationList(){
        CycleOperationEntity entity = mock(CycleOperationEntity.class);
        CycleOperationEntity entity1 = mock(CycleOperationEntity.class);

        List<CycleOperationEntity> list = new ArrayList<>(5);
        list.add(entity);
        list.add(entity1);

        List<CycleOperation> entities = dataMapper.toEntity(list);

        assertThat(entities, is(notNullValue()));
        assertThat(entities.size(), is(2));

        assertThat(entities.get(0), is(instanceOf(CycleOperation.class)));
        assertThat(entities.get(1), is(instanceOf(CycleOperation.class)));

        assertThat(entities.get(0), is(notNullValue()));
        assertThat(entities.get(1), is(notNullValue()));

        assertThat(entities.get(0).getTransaction(),is(notNullValue()));
        assertThat(entities.get(1).getTransaction(),is(notNullValue()));
    }

    private CycleOperationEntity createCycleOperationEntity(){
        return new CycleOperationEntity(FAKE_ID,FAKE_NAME,entity,FAKE_INTERVAL);
    }

    private CycleOperation createCycleOperation(){
        return new CycleOperation(FAKE_ID,transaction,FAKE_NAME,FAKE_INTERVAL);
    }*/
}
