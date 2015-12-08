package com.bcgogo.txn;

import com.bcgogo.AbstractTest;
import com.bcgogo.customer.CustomerController;
import com.bcgogo.customer.MemberController;
import com.bcgogo.enums.ConsumeType;
import com.bcgogo.enums.OrderStatus;
import com.bcgogo.enums.OrderTypes;
import com.bcgogo.enums.TimesStatus;
import com.bcgogo.enums.assistantStat.AssistantRecordType;
import com.bcgogo.product.dto.ProductDTO;
import com.bcgogo.product.dto.ProductLocalInfoDTO;
import com.bcgogo.product.service.IProductService;
import com.bcgogo.search.dto.OrderIndexDTO;
import com.bcgogo.search.model.InventorySearchIndex;
import com.bcgogo.search.service.IOrderIndexService;
import com.bcgogo.search.service.ISearchService;
import com.bcgogo.service.ServiceManager;
import com.bcgogo.stat.BusinessStatController;
import com.bcgogo.stat.service.IBizStatService;
import com.bcgogo.txn.dto.*;
import com.bcgogo.txn.model.assistantStat.ShopAchievementConfig;
import com.bcgogo.txn.service.ITxnService;
import com.bcgogo.txn.service.TxnService;
import com.bcgogo.txn.service.stat.assistantStat.IAssistantStatService;
import com.bcgogo.user.dto.*;
import com.bcgogo.user.service.IMembersService;
import com.bcgogo.utils.DateUtil;
import com.bcgogo.utils.JsonUtil;
import com.bcgogo.utils.NumberUtil;
import junit.framework.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ModelMap;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuWei
 * Date: 12-4-24
 * Time: 上午10:31
 * To change this template use File | Settings | File Templates.
 */
public class BcgogoEventPublisherTest extends AbstractTest {

  protected BusinessStatController businessStatController;

  @Before
  public void setUp() throws Exception {
    txnController = new TxnController();
    goodsStorageController = new GoodStorageController();
    businessStatController = new BusinessStatController();
    goodsReturnController = new GoodsReturnController();
    txnController = new TxnController();
    goodsStorageController = new GoodStorageController();
    buyController = new RFGoodBuyController();
    saleController = new GoodSaleController();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    washBeautyController = new WashBeautyController();
    categoryController = new CategoryController();
    memberController = new MemberController();
    customerController = new CustomerController();
    modelMap = new ModelMap();
    repairController = new RepairController();
    initTxnControllers(goodsStorageController, txnController, repairController, saleController);
    ITxnService txnService = ServiceManager.getService(ITxnService.class);
    ISearchService searchService = ServiceManager.getService(ISearchService.class);
    IMembersService membersService = ServiceManager.getService(IMembersService.class);
  }


  @Test
  //维修施工单保存测试
  public void repairOrderSavedTest() throws Exception {
    IBizStatService bizStatService = ServiceManager.getService(IBizStatService.class);

    long startTime = System.currentTimeMillis();

    ModelMap model = new ModelMap();
    VehicleDTO vehicleDTO = addVehicle();


    String vehicleNumber = new String("苏A00098");
    request.setParameter("vehicleNumber", vehicleDTO.getLicenceNo());

    Long shopId = createShop();
    request.getSession().setAttribute("shopId", shopId);
    String url = txnController.getRepairOrderByVehicleNumber(model, request, response);
    RepairOrderDTO repairOrderDTO = (RepairOrderDTO) model.get("repairOrderDTO");
    //RepairOrderDTO repairOrderDTO = new RepairOrderDTO();
    repairOrderDTO.setShopId(shopId);

    Assert.assertEquals(vehicleNumber, repairOrderDTO.getLicenceNo());
    Assert.assertEquals(0, repairOrderDTO.getDebt(), 0.001);
    Assert.assertEquals("/txn/invoicing", url);


    //生成一个客户
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setMobile("13015673686");
    customerDTO.setLandLine("0512-88781010");
    customerDTO.setContact("单元测试联系人01");
    customerDTO.setName("单元测试联系人02");
    customerDTO.setShopId(shopId);


    repairOrderDTO.setBrand("雪佛兰");
    repairOrderDTO.setModel("赛欧");
    repairOrderDTO.setYear("2011");
    repairOrderDTO.setEngine("1.4L");

    repairOrderDTO.setCustomerName(customerDTO.getName());
    repairOrderDTO.setMobile(customerDTO.getMobile());
    repairOrderDTO.setLandLine(customerDTO.getLandLine());
    repairOrderDTO.setLicenceNo(vehicleDTO.getLicenceNo());
    repairOrderDTO.setVechicle(vehicleDTO.getLicenceNo());
    repairOrderDTO.setVechicleId(vehicleDTO.getId());
    repairOrderDTO.setDebt(0);
    String startTimeStr = "2012-3-20 12:00";
    repairOrderDTO.setStartDateStr(startTimeStr);
    repairOrderDTO.setEndDateStr(startTimeStr);


    repairOrderDTO.setTotal(340D);
    repairOrderDTO.setHuankuanTime("");//还款时间设置为""

    RepairOrderServiceDTO serviceDTO = new RepairOrderServiceDTO();
    serviceDTO.setService("work");
    serviceDTO.setTotal(100d);
    serviceDTO.setMemo("work");
    serviceDTO.setConsumeType(ConsumeType.MONEY);
    serviceDTO.setWorkers("维修单施工人");
    RepairOrderServiceDTO[] repairOrderServiceDTOs = new RepairOrderServiceDTO[1];
    repairOrderServiceDTOs[0] = serviceDTO;
    repairOrderDTO.setServiceDTOs(repairOrderServiceDTOs);

    RepairOrderItemDTO repairOrderItemDTO1 = new RepairOrderItemDTO();
    repairOrderItemDTO1.setBrand("brand");
    repairOrderItemDTO1.setProductName("product");
    repairOrderItemDTO1.setMemo("xiaoshou");
    repairOrderItemDTO1.setPurchasePrice(100d);
    repairOrderItemDTO1.setPrice((120d));
    repairOrderItemDTO1.setInventoryAmount(0d);
    repairOrderItemDTO1.setAmount(2D);
    repairOrderItemDTO1.setProductType(1);
    repairOrderItemDTO1.setTotal(240d);
    RepairOrderItemDTO[] repairOrderItemDTOs = new RepairOrderItemDTO[1];
    repairOrderItemDTOs[0] = repairOrderItemDTO1;
    repairOrderDTO.setItemDTOs(repairOrderItemDTOs);

    repairOrderDTO.setProductSaler("维修单销售人");
//    repairOrderDTO.setServiceWorker("维修单销售人");
    repairOrderDTO.setStatus(OrderStatus.REPAIR_DONE);
    //orderRunBusinessStat(repairOrderDTO);

//    request.setParameter("isRunThread", "noRun");
//    url = txnController.saveRepairOrder(model, repairOrderDTO, request, "account");
    url = repairController.accountRepairOrder(model, repairOrderDTO, request);
    unitTestSleepSecond();
    Assert.assertNotNull(repairOrderDTO.getId());//判断维修美容单是否新建成功
    long endTime = System.currentTimeMillis();
    startTime = DateUtil.convertDateStringToDateLong(DateUtil.DATE_STRING_FORMAT_DEFAULT,startTimeStr);
    List<OrderIndexDTO> orderIndexDTOList = orderIndexService.getOrderIndexByServiceWork(shopId, "", startTime-10000, startTime+10000, 0, 100);
    Assert.assertEquals(1, orderIndexDTOList.size());
    OrderIndexDTO orderIndexDTO = orderIndexDTOList.get(0);
    Assert.assertEquals("维修单施工人, 维修单销售人", orderIndexDTO.getServiceWorker());

    IAssistantStatService assistantStatService = ServiceManager.getService(IAssistantStatService.class);
    assistantStatService.assistantStat(shopId);
    for (RepairOrderItemDTO repairOrderItemDTO : repairOrderDTO.getItemDTOs()) {
      ShopAchievementConfig achievementConfig = assistantStatService.getShopAchievementConfig(shopId, repairOrderItemDTO.getProductId(), AssistantRecordType.PRODUCT);
      Assert.assertEquals(repairOrderItemDTO.getProductId(), achievementConfig.getAchievementRecordId());
    }

  }


  @Test
  public void salesOrderSavedTest() throws Exception {
    Long shopId = createShop();
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("MyProduct");
    productDTO.setSpec("1232");
    productDTO.setProductVehicleBrand("多款");
    addInventory(shopId, productDTO, 100, 10);

    InventorySearchIndex inventorySearchIndex = searchWriter.getById(InventorySearchIndex.class, productDTO.getProductLocalInfoId());
    Assert.assertEquals(100, inventorySearchIndex.getAmount(), 0.001);

    SalesOrderDTO salesOrderDTO1 = createSalesOrderDTO(productDTO, 30, 100, 3000);
    salesOrderDTO1.setGoodsSaler("单元测试销售人111");
    salesOrderDTO1.setShopId(shopId);
    orderRunBusinessStat(salesOrderDTO1);
//    request.setParameter("isRunThread", "noRun");
    ModelMap model = new ModelMap();
    saleController.saveSale(model, salesOrderDTO1, request, response, null);
    unitTestSleepSecond();
    saleController.getSalesOrder(model, request, salesOrderDTO1.getId().toString());
    SalesOrderDTO salesOrderDTO = (SalesOrderDTO) model.get("salesOrderDTO");
    long vestDate = salesOrderDTO.getVestDate();
    List<OrderIndexDTO> orderIndexDTOList = orderIndexService.getOrderIndexByServiceWork(shopId, "单元测试销售人111", vestDate-100, vestDate+100, 0, 100);
    Assert.assertEquals(1, orderIndexDTOList.size());
    OrderIndexDTO orderIndexDTO = orderIndexDTOList.get(0);
    Assert.assertEquals("单元测试销售人111", orderIndexDTO.getServiceWorker());
  }

  @Test
  public void purchaseOrderSavedTest() throws Exception {
    ModelMap model = new ModelMap();
    Long shopId = createShop();
    request.getSession().setAttribute("shopId", shopId);
    PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
    purchaseOrderDTO.setSupplier("pTest");
    purchaseOrderDTO.setContact("HHH");
    purchaseOrderDTO.setMobile("1301234393");
    purchaseOrderDTO.setDeliveryDateStr("2012-02-24");
    purchaseOrderDTO.setVestDateStr("2012-02-24 12:20");
    purchaseOrderDTO.setTotal(100);

    PurchaseOrderItemDTO purchaseOrderItemDTO1 = new PurchaseOrderItemDTO();
    purchaseOrderItemDTO1.setAmount(11D);
    purchaseOrderItemDTO1.setPrice(1D);
    String productName = "QQQ" + System.currentTimeMillis();
    purchaseOrderItemDTO1.setProductName(productName);
    purchaseOrderItemDTO1.setSpec("123");
    purchaseOrderItemDTO1.setTotal(11D);
    purchaseOrderItemDTO1.setVehicleBrand("多款");
    purchaseOrderItemDTO1.setPrice(100D);


    PurchaseOrderItemDTO[] itemDTOs = new PurchaseOrderItemDTO[1];
    itemDTOs[0] = purchaseOrderItemDTO1;
    purchaseOrderDTO.setItemDTOs(itemDTOs);
//    request.setParameter("isRunThread", "noRun");

    buyController.save(request, model, purchaseOrderDTO);
    unitTestSleepSecond();
    buyController.show(model, purchaseOrderDTO.getId().toString(), request);
    PurchaseOrderDTO newPurchaseOrderDTO = (PurchaseOrderDTO) model.get("purchaseOrderDTO");

  }

  @Test
  public void PurchaseInventoryOrderSavedTest() throws Exception {
    ModelMap model = new ModelMap();
    Long shopId = createShop();
    request.getSession().setAttribute("shopId", shopId);
    PurchaseInventoryDTO purchaseInventoryDTO = new PurchaseInventoryDTO();
    purchaseInventoryDTO.setSupplier("pTest1");
    purchaseInventoryDTO.setContact("HHH");
    purchaseInventoryDTO.setMobile("1301234394");
    purchaseInventoryDTO.setVestDateStr("2012-03-20 12:20");
    purchaseInventoryDTO.setTotal(11D);
    purchaseInventoryDTO.setShopId(shopId);

    PurchaseInventoryItemDTO purchaseInventoryItemDTO1 = new PurchaseInventoryItemDTO();
    purchaseInventoryItemDTO1.setAmount(25d);
    purchaseInventoryItemDTO1.setPurchasePrice(1d);
    String productName = "QQQ" + System.currentTimeMillis();
    purchaseInventoryItemDTO1.setProductName(productName);
    purchaseInventoryItemDTO1.setSpec("123");
    purchaseInventoryItemDTO1.setTotal(11d);
    purchaseInventoryItemDTO1.setVehicleBrand("全部");
    purchaseInventoryItemDTO1.setBarcode("1234567890123");

    PurchaseInventoryItemDTO[] itemDTOs = new PurchaseInventoryItemDTO[1];
    itemDTOs[0] = purchaseInventoryItemDTO1;
    purchaseInventoryDTO.setItemDTOs(itemDTOs);
    goodsStorageController.savePurchaseInventory(model, purchaseInventoryDTO, request, response);
    unitTestSleepSecond();
    goodsStorageController.getPurchaseInventory(model, request, purchaseInventoryDTO.getId().toString());
    purchaseInventoryDTO = (PurchaseInventoryDTO) model.get("purchaseInventoryDTO");
  }

  public void orderRunBusinessStat(BcgogoOrderDto bcgogoOrderDto) {

    Assert.assertNotNull(bcgogoOrderDto);//判断洗车单是否新建成功
    // Assert.assertNotNull(bcgogoOrderDto.getShopId());//判断洗车单是否新建成功

    IProductService productService = ServiceManager.getService(IProductService.class);
    ITxnService txnService = ServiceManager.getService(ITxnService.class);


    Long shopId = bcgogoOrderDto.getShopId();

    double washNum = 0.0;  //洗车单金额
    double salesTotal = 0.0; //销售单金额 或者 施工单中的销售金额
    double serviceTotal = 0.0; //服务金额
    double productCostTotal = 0.0; //商品成本
    double memberIncome = 0.0;//会员营业统计收入
    //每来一个单子 更新营业业绩
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    calendar.setTime(date);
    calendar.setMinimalDaysInFirstWeek(1);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    long year = calendar.get(Calendar.YEAR);
    long month = calendar.get(Calendar.MONTH) + 1;
    long day = calendar.get(Calendar.DAY_OF_MONTH);

    int size = 1;
    int count = 0;

    //查询今天的数据
    BusinessStatDTO todayStatDto = new BusinessStatDTO();
    List<BusinessStatDTO> todayList = txnService.getBusinessStatByYearMonthDay(shopId, year, month, day);
    if (todayList.size() > 0) {
      todayStatDto = todayList.get(0);
    }

    if (bcgogoOrderDto instanceof WashOrderDTO) {
      WashOrderDTO washOrderDTO = (WashOrderDTO) bcgogoOrderDto;
      shopId = washOrderDTO.getShopId();
      washNum = washOrderDTO.getCashNum();
    } else if (bcgogoOrderDto instanceof RepairOrderDTO) {
      RepairOrderDTO repairOrderDTO = (RepairOrderDTO) bcgogoOrderDto;
      shopId = repairOrderDTO.getShopId();
      for (RepairOrderServiceDTO repairOrderServiceDTO : repairOrderDTO.getServiceDTOs()) {
        if (repairOrderServiceDTO == null || repairOrderServiceDTO.getTotal() <= 0) {
          continue;
        }
        serviceTotal += repairOrderServiceDTO.getTotal();
      }
      for (RepairOrderItemDTO repairOrderItemDTO : repairOrderDTO.getItemDTOs()) {
        if (repairOrderItemDTO == null || repairOrderItemDTO.getTotal() <= 0 || repairOrderItemDTO.getProductId() == null) {
          continue;
        }
        salesTotal += repairOrderItemDTO.getTotal();

        long id = repairOrderItemDTO.getProductId();
        ProductLocalInfoDTO productLocalInfoDTO = productService.getProductLocalInfoById(id, shopId);
        if (productLocalInfoDTO != null) {
          double productPrice = productLocalInfoDTO.getPurchasePrice();
          double amount = repairOrderItemDTO.getAmount();
          productCostTotal += (productPrice * amount);
        }
      }
    } else if (bcgogoOrderDto instanceof SalesOrderDTO) {
      SalesOrderDTO salesOrderDTO = (SalesOrderDTO) bcgogoOrderDto;
      shopId = salesOrderDTO.getShopId();
      SalesOrderItemDTO[] salesOrderItemDTOs = salesOrderDTO.getItemDTOs();
      if (salesOrderItemDTOs != null && salesOrderItemDTOs.length > 0) {
        for (SalesOrderItemDTO salesOrderItemDTO : salesOrderItemDTOs) {
          if (salesOrderItemDTO == null || salesOrderItemDTO.getTotal() <= 0 || salesOrderItemDTO.getProductId() == null) {
            continue;
          }
          salesTotal += salesOrderItemDTO.getTotal();
          long id = salesOrderItemDTO.getProductId();
          ProductLocalInfoDTO productLocalInfoDTO = productService.getProductLocalInfoById(id, shopId);
          if (productLocalInfoDTO != null) {
            double productPrice = productLocalInfoDTO.getPurchasePrice() == null ? 0d : productLocalInfoDTO.getPurchasePrice();
            double amount = salesOrderItemDTO.getAmount();
            productCostTotal += (productPrice * amount);
          }
        }
      }
    } else if (bcgogoOrderDto instanceof WashBeautyOrderDTO) {
      WashBeautyOrderDTO washBeautyOrderDTO = (WashBeautyOrderDTO) bcgogoOrderDto;
      shopId = washBeautyOrderDTO.getShopId();
      washNum = washBeautyOrderDTO.getSettledAmount();
    } else if (bcgogoOrderDto instanceof MemberCardOrderDTO) {
      MemberCardOrderDTO memberCardOrderDTO = (MemberCardOrderDTO) bcgogoOrderDto;
      shopId = memberCardOrderDTO.getShopId();
      ReceivableDTO receivableDTO = memberCardOrderDTO.getReceivableDTO();
      //营业额 实收 + 欠款
      memberIncome = receivableDTO.getSettledAmount() + receivableDTO.getDebt();
      //保留一位小数  改为2位小数
      memberIncome = NumberUtil.toReserve(memberIncome, NumberUtil.MONEY_PRECISION);
    } else {
      //如果不是这三种单子 则报错
      Assert.assertEquals(1, 0);
    }


    List<BusinessStatDTO> businessStatDTOList = txnService.getLatestBusinessStat(shopId, year, size);
    if (businessStatDTOList == null || businessStatDTOList.size() <= 0) {
      //昨天的数据
      BusinessStatDTO yesterdayDto = new BusinessStatDTO();
      yesterdayDto.setShopId(shopId);
      yesterdayDto.setStatYear(year);
      yesterdayDto.setStatMonth(month);
      yesterdayDto.setStatDay(day - 1);
      yesterdayDto.setSales(0.0);
      yesterdayDto.setService(0.0);
      yesterdayDto.setWash(0.0);
      yesterdayDto.setStatSum(0.0);
      yesterdayDto.setProductCost(0.0);
      yesterdayDto.setMemberIncome(0.0);
      yesterdayDto.setStatTime(System.currentTimeMillis());
      txnService.saveBusinessStat(yesterdayDto);

      //今天的数据
      BusinessStatDTO businessStatDTO = new BusinessStatDTO();
      businessStatDTO.setShopId(shopId);
      businessStatDTO.setStatYear(year);
      businessStatDTO.setStatMonth(month);
      businessStatDTO.setStatDay(day);
      businessStatDTO.setSales(salesTotal);
      businessStatDTO.setService(serviceTotal);
      businessStatDTO.setWash(washNum);
      businessStatDTO.setStatSum(washNum + salesTotal + serviceTotal + memberIncome);
      businessStatDTO.setProductCost(productCostTotal);
      businessStatDTO.setMemberIncome(memberIncome);
      businessStatDTO.setStatTime(System.currentTimeMillis());
      txnService.saveBusinessStat(businessStatDTO);


    } else {
      BusinessStatDTO businessStatDTO = businessStatDTOList.get(0);
      if (businessStatDTO.getStatYear() == year && businessStatDTO.getStatMonth() == month && businessStatDTO.getStatDay() == day) {
        businessStatDTO.setShopId(shopId);
        businessStatDTO.setStatYear(year);
        businessStatDTO.setStatMonth(month);
        businessStatDTO.setStatDay(day);
        businessStatDTO.setSales(businessStatDTO.getSales() + salesTotal);
        businessStatDTO.setService(businessStatDTO.getService() + serviceTotal);
        businessStatDTO.setWash(businessStatDTO.getWash() + washNum);
        businessStatDTO.setStatSum(businessStatDTO.getStatSum() + washNum + serviceTotal + salesTotal + memberIncome);
        businessStatDTO.setProductCost(businessStatDTO.getProductCost() + productCostTotal);
        businessStatDTO.setMemberIncome(businessStatDTO.getMemberIncome() + memberIncome);
        businessStatDTO.setStatTime(System.currentTimeMillis());
        txnService.updateBusinessStat(businessStatDTO);
      } else {
        //自动补全营业数据,即补全查询到的日期 和单据当天之间的营业数据
        int statMonth = businessStatDTO.getStatMonth().intValue();
        int statDay = businessStatDTO.getStatDay().intValue();
        int yearTmp = (int) year;

        for (int monthTmp = statMonth; monthTmp <= month; monthTmp++) {
          //last day of current month
          calendar.set(yearTmp, monthTmp - 1, 1, 0, 0, 0);
          int lastDayOfCurrentMonth = calendar.getActualMaximum(Calendar.DATE);

          int da = new Date().getDate();

          if (month == monthTmp) {
            lastDayOfCurrentMonth = da - 1;
          }

          int dayTmp1 = 1;
          if (statMonth == monthTmp) {
            dayTmp1 = statDay + 1;
          }

          for (int dayTmp = dayTmp1; dayTmp <= lastDayOfCurrentMonth; dayTmp++) {
            BusinessStatDTO newBusinessStatDTO = new BusinessStatDTO();
            newBusinessStatDTO.setShopId(shopId);
            newBusinessStatDTO.setStatYear(year);
            newBusinessStatDTO.setStatMonth((long) monthTmp);
            newBusinessStatDTO.setStatDay((long) dayTmp);
            newBusinessStatDTO.setSales(businessStatDTO.getSales());
            newBusinessStatDTO.setService(businessStatDTO.getService());
            newBusinessStatDTO.setWash(businessStatDTO.getWash());
            newBusinessStatDTO.setStatSum(businessStatDTO.getStatSum());
            newBusinessStatDTO.setProductCost(businessStatDTO.getProductCost());
            newBusinessStatDTO.setMemberIncome(businessStatDTO.getMemberIncome());
            newBusinessStatDTO.setStatTime(System.currentTimeMillis());
            txnService.saveBusinessStat(newBusinessStatDTO);
          }
        }
        BusinessStatDTO newBusinessStatDTO = new BusinessStatDTO();
        newBusinessStatDTO.setShopId(shopId);
        newBusinessStatDTO.setStatYear(year);
        newBusinessStatDTO.setStatMonth(month);
        newBusinessStatDTO.setStatDay(day);
        newBusinessStatDTO.setSales(businessStatDTO.getSales() + salesTotal);
        newBusinessStatDTO.setService(businessStatDTO.getService() + serviceTotal);
        newBusinessStatDTO.setWash(businessStatDTO.getWash() + washNum);
        newBusinessStatDTO.setStatSum(businessStatDTO.getStatSum() + washNum + salesTotal + serviceTotal + memberIncome);
        newBusinessStatDTO.setProductCost(businessStatDTO.getProductCost() + productCostTotal);
        newBusinessStatDTO.setMemberIncome(businessStatDTO.getMemberIncome() + memberIncome);
        newBusinessStatDTO.setStatTime(System.currentTimeMillis());
        txnService.saveBusinessStat(newBusinessStatDTO);
      }
    }

    //查询今天的数据 单子录了之后判断今天的营业数据
    List<BusinessStatDTO> newTodayList = txnService.getBusinessStatByYearMonthDay(shopId, year, month, day);
    Assert.assertEquals(1, newTodayList.size());
    BusinessStatDTO newTodayStatDto = newTodayList.get(0);
    Assert.assertNotNull(newTodayStatDto);

    Assert.assertEquals(washNum, newTodayStatDto.getWash() - todayStatDto.getWash(), 0.001);
    Assert.assertEquals(salesTotal, newTodayStatDto.getSales() - todayStatDto.getSales(), 0.001);
    Assert.assertEquals(serviceTotal, newTodayStatDto.getService() - todayStatDto.getService(), 0.001);
    Assert.assertEquals(productCostTotal, newTodayStatDto.getProductCost() - todayStatDto.getProductCost(), 0.001);
    Assert.assertEquals(memberIncome, newTodayStatDto.getMemberIncome() - todayStatDto.getMemberIncome(), 0.001);
    //查询昨天的数据
    List<BusinessStatDTO> newYesterdayList = txnService.getBusinessStatByYearMonthDay(shopId, year, month, day - 1);
    Assert.assertEquals(1, newYesterdayList.size());

    List<BusinessStatDTO> monthList = businessStatController.getMonthBusinessStatList(shopId, (int) year);
    Assert.assertNotNull(monthList);

    List<BusinessStatDTO> dayList = businessStatController.getDayBusinessStatList(shopId, (int) year, (int) month);
    Assert.assertNotNull(dayList);
  }


  @Test
  public void testCreateWashBeautyOrder() throws Exception {
//    ModelMap model = new ModelMap();
//    Long shopId = createShop();
//    request.getSession().setAttribute("shopId", shopId);
//    saveServiceAndCategory(shopId);
//    ServiceDTO[] serviceDTOs = rfiTxnService.getServiceByWashBeauty(shopId, null);
//    WashBeautyOrderDTO w = saveMemberInfoDetail(shopId, serviceDTOs[0].getId());
//    washBeautyController.createWashBeautyOrder(model, request, null);
//    WashBeautyOrderDTO washBeautyOrderDTO = (WashBeautyOrderDTO) model.get("washBeautyOrderDTO");
//    serviceDTOs = washBeautyOrderDTO.getServiceDTOs();
//    Assert.assertEquals(1, serviceDTOs.length);
//    ServiceDTO serviceDTO = serviceDTOs[0];
//    Assert.assertEquals("洗车", serviceDTO.getName());
//    Assert.assertEquals(20, serviceDTO.getPrice(), 0.001);
//    Assert.assertEquals(5, serviceDTO.getPercentageAmount(), 0.001);
//
//    request.setParameter("customerId", String.valueOf(w.getCustomerId()));
//    washBeautyController.getCustomerInfoByName(model, request, null, null, null);
//    washBeautyOrderDTO = (WashBeautyOrderDTO) model.get("washBeautyOrderDTO");
//    serviceDTOs = washBeautyOrderDTO.getServiceDTOs();
//    Assert.assertEquals(1, serviceDTOs.length);
//    serviceDTO = serviceDTOs[0];
//    Assert.assertEquals("洗车", serviceDTO.getName());
//    Assert.assertEquals(20, serviceDTO.getPrice(), 0.001);
//    Assert.assertEquals(5, serviceDTO.getPercentageAmount(), 0.001);
//    Assert.assertEquals("周东明", washBeautyOrderDTO.getCustomer());
//    Assert.assertEquals(w.getCustomerId(), washBeautyOrderDTO.getCustomerId());
//    Assert.assertEquals("苏E00000", washBeautyOrderDTO.getLicenceNo());
//    Assert.assertEquals("15995496255", washBeautyOrderDTO.getMobile());
//    Assert.assertEquals("奥迪", washBeautyOrderDTO.getBrand());
//    Assert.assertEquals("TT", washBeautyOrderDTO.getModel());
//    MemberDTO memberDTO = washBeautyOrderDTO.getMemberDTO();
//    List<MemberServiceDTO> memberServiceDTOs = memberDTO.getMemberServiceDTOs();
//    Assert.assertEquals(1, memberServiceDTOs.size());
//    MemberServiceDTO memberServiceDTO = memberServiceDTOs.get(0);
//    Assert.assertEquals(2000d, memberDTO.getBalance(), 0.001);
//    Assert.assertEquals(w.getCustomerId(), memberDTO.getCustomerId());
//    Assert.assertEquals("苏E00000", memberDTO.getMemberNo());
//    Assert.assertEquals(serviceDTO.getId(), memberServiceDTO.getServiceId());
//    Assert.assertEquals(20, memberServiceDTO.getTimes().intValue());
//    Assert.assertEquals("洗车", memberServiceDTO.getServiceName());
//    Assert.assertEquals(DateUtil.getDeadline(System.currentTimeMillis(), 1), memberServiceDTO.getDeadline());
//
//    request.setParameter("licenceNo", w.getLicenceNo());
//    washBeautyController.getCustomerInfoByLicenceNo(model, request, null, null, null);
//    washBeautyOrderDTO = (WashBeautyOrderDTO) model.get("washBeautyOrderDTO");
//    serviceDTOs = washBeautyOrderDTO.getServiceDTOs();
//    Assert.assertEquals(1, serviceDTOs.length);
//    serviceDTO = serviceDTOs[0];
//    Assert.assertEquals("洗车", serviceDTO.getName());
//    Assert.assertEquals(20, serviceDTO.getPrice(), 0.001);
//    Assert.assertEquals(5, serviceDTO.getPercentageAmount(), 0.001);
//    Assert.assertEquals("周东明", washBeautyOrderDTO.getCustomer());
//    Assert.assertEquals(w.getCustomerId(), washBeautyOrderDTO.getCustomerId());
//    Assert.assertEquals("苏E00000", washBeautyOrderDTO.getLicenceNo());
//    Assert.assertEquals("15995496255", washBeautyOrderDTO.getMobile());
//    Assert.assertEquals("奥迪", washBeautyOrderDTO.getBrand());
//    Assert.assertEquals("TT", washBeautyOrderDTO.getModel());
//    memberDTO = washBeautyOrderDTO.getMemberDTO();
//    memberServiceDTOs = memberDTO.getMemberServiceDTOs();
//    Assert.assertEquals(1, memberServiceDTOs.size());
//    memberServiceDTO = memberServiceDTOs.get(0);
//    Assert.assertEquals(2000d, memberDTO.getBalance(), 0.001);
//    Assert.assertEquals(w.getCustomerId(), memberDTO.getCustomerId());
//    Assert.assertEquals("苏E00000", memberDTO.getMemberNo());
//    Assert.assertEquals(serviceDTO.getId(), memberServiceDTO.getServiceId());
//    Assert.assertEquals(20, memberServiceDTO.getTimes().intValue());
//    Assert.assertEquals("洗车", memberServiceDTO.getServiceName());
//    Assert.assertEquals(DateUtil.getDeadline(System.currentTimeMillis(), 1), memberServiceDTO.getDeadline());
//
//    categoryController.createNewService(model, request);
//    CategoryServiceSearchDTO categoryServiceSearchDTO = (CategoryServiceSearchDTO) model.get("categoryServiceSearchDTO");
//    categoryServiceSearchDTO.setServiceName("打蜡");
//    categoryServiceSearchDTO.setCategoryName("美容");
//    categoryServiceSearchDTO.setPrice(35d);
//    categoryServiceSearchDTO.setPercentageAmount(10d);
////    categoryController.addNewService(model, request, categoryServiceSearchDTO);
//    categoryServiceSearchDTO = (CategoryServiceSearchDTO) model.get("categoryServiceSearchDTO");
//    ServiceDTO serviceDTO2 = categoryServiceSearchDTO.getServiceDTOs()[1];
//
//    WashBeautyOrderItemDTO[] washBeautyOrderItemDTOs = new WashBeautyOrderItemDTO[2];
//    washBeautyOrderItemDTOs[0] = new WashBeautyOrderItemDTO();
//    washBeautyOrderItemDTOs[0].setPayType(ConsumeType.TIMES);
//    washBeautyOrderItemDTOs[0].setPrice(20d);
//    washBeautyOrderItemDTOs[0].setServiceId(serviceDTO.getId());
//    washBeautyOrderItemDTOs[0].setConsumeTypeStr(ConsumeType.TIMES);
//    washBeautyOrderItemDTOs[0].setShopId(shopId);
//    washBeautyOrderItemDTOs[0].setSurplusTimes("20");
//    washBeautyOrderItemDTOs[1] = new WashBeautyOrderItemDTO();
//    washBeautyOrderItemDTOs[1].setPayType(ConsumeType.MONEY);
//    washBeautyOrderItemDTOs[1].setPrice(35d);
//    washBeautyOrderItemDTOs[1].setServiceId(serviceDTO2.getId());
//    washBeautyOrderItemDTOs[1].setConsumeTypeStr(ConsumeType.MONEY);
//    washBeautyOrderItemDTOs[1].setShopId(shopId);
//    washBeautyOrderItemDTOs[1].setSurplusTimes(null);
//    washBeautyOrderDTO.setWashBeautyOrderItemDTOs(washBeautyOrderItemDTOs);
//    washBeautyOrderDTO.setTotal(35d);
//    washBeautyOrderDTO.setSettledAmount(35d);
//    washBeautyOrderDTO.setAccountMemberNo("苏E00000");
//    washBeautyOrderDTO.setBankAmount(0d);
//    washBeautyOrderDTO.setBankCheckAmount(0d);
//    washBeautyOrderDTO.setBankCheckNo("");
//    washBeautyOrderDTO.setCashAmount(0d);
//    washBeautyOrderDTO.setOrderDiscount(0);
//    washBeautyOrderDTO.setDebt(0);
//    washBeautyOrderDTO.setMemberAmount(35d);
//
//    washBeautyOrderDTO = rfiTxnService.saveWashBeautyOrderAndSendSms(shopId, 0L, washBeautyOrderDTO);
//
//    orderRunBusinessStat(washBeautyOrderDTO);
//
//
//    washBeautyOrderDTO.setMemberDTO(membersService.getMemberByCustomerId(shopId, washBeautyOrderDTO.getCustomerId()));
//    if (washBeautyOrderDTO.getMemberDTO() != null && washBeautyOrderDTO.getMemberDTO().getMemberServiceDTOs() != null) {
//      for (MemberServiceDTO dto : washBeautyOrderDTO.getMemberDTO().getMemberServiceDTOs()) {
//        Service service = rfiTxnService.getServiceById(dto.getServiceId());
//        if (service != null) {
//          dto.setServiceName(service.getName());
//        }
//      }
//    }
//    washBeautyOrderDTO.setServiceDTOs(rfiTxnService.getServiceByWashBeauty(shopId, washBeautyOrderDTO.getMemberDTO()));
//    Double total = 0d;
//    for (int i = 0; i < washBeautyOrderDTO.getWashBeautyOrderItemDTOs().length; i++) {
//      for (ServiceDTO dto : washBeautyOrderDTO.getServiceDTOs()) {
//        if (washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[i].getServiceId().equals(dto.getId())) {
//          washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[i].setSurplusTimes(dto.getSurplusTimes());
//          break;
//        }
//      }
//      if (!ConsumeType.TIMES.equals(washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[i].getConsumeTypeStr())) {
//        total += washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[i].getPrice();
//      }
//    }
//    washBeautyOrderDTO.setTotal(total);
//    washBeautyController.getTotalDebts(washBeautyOrderDTO.getCustomerId(), shopId, model);
//    washBeautyController.getTotalConsume(washBeautyOrderDTO.getCustomerId(), shopId, model);
//    model.put("washBeautyOrderDTO", washBeautyOrderDTO);
//
//
//    washBeautyOrderDTO = (WashBeautyOrderDTO) model.get("washBeautyOrderDTO");
//    memberDTO = washBeautyOrderDTO.getMemberDTO();
//    Assert.assertEquals(1965d, memberDTO.getBalance(), 0.001);
//    MemberServiceDTO memberServiceDTO1 = memberDTO.getMemberServiceDTOs().get(0);
//    Assert.assertEquals(19, memberServiceDTO1.getTimes().intValue());
//    ReceivableDTO receivableDTO = txnService.getReceivableByShopIdAndOrderTypeAndOrderId(shopId, OrderTypes.WASH_BEAUTY, washBeautyOrderDTO.getId());
//    Assert.assertEquals(washBeautyOrderDTO.getCashAmount(), receivableDTO.getCash(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getTotal(), receivableDTO.getTotal(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getBankCheckAmount(), receivableDTO.getCheque(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getDebt(), receivableDTO.getDebt(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getOrderDiscount(), receivableDTO.getDiscount(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getMemberAmount(), receivableDTO.getMemberBalancePay(), 0.001);
//    Assert.assertEquals(washBeautyOrderDTO.getSettledAmount(), receivableDTO.getSettledAmount(), 0.001);
//    Assert.assertEquals(OrderTypes.WASH_BEAUTY, receivableDTO.getOrderType());
//    Assert.assertEquals(shopId, receivableDTO.getShopId());
//    Assert.assertEquals(washBeautyOrderDTO.getId(), receivableDTO.getOrderId());
//    List<ItemIndexDTO> itemIndexDTOs = searchService.getItemIndexDTOListByOrderId(shopId, washBeautyOrderDTO.getId());
//    Assert.assertEquals(2, itemIndexDTOs.size());
//    ItemIndexDTO itemIndexDTO1 = itemIndexDTOs.get(0);
//    ItemIndexDTO itemIndexDTO2 = itemIndexDTOs.get(1);
//    WashBeautyOrderItemDTO washBeautyOrderItemDTO1 = washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[0];
//    WashBeautyOrderItemDTO washBeautyOrderItemDTO2 = washBeautyOrderDTO.getWashBeautyOrderItemDTOs()[1];
//    Assert.assertEquals(OrderStatus.WASH_SETTLED, itemIndexDTO1.getOrderStatus());
//    Assert.assertEquals(washBeautyOrderDTO.getCustomerId(), itemIndexDTO1.getCustomerId());
//    Assert.assertEquals(washBeautyOrderDTO.getCustomer(), itemIndexDTO1.getCustomerOrSupplierName());
//    Assert.assertEquals(washBeautyOrderItemDTO1.getPrice(), itemIndexDTO1.getItemPrice(), 0.001);
//    Assert.assertEquals(0, itemIndexDTO1.getItemCostPrice(), 0.001);
//    Assert.assertEquals(washBeautyOrderItemDTO1.getId(), itemIndexDTO1.getItemId());
//    Assert.assertEquals(washBeautyOrderItemDTO1.getServiceId(), itemIndexDTO1.getServiceId());
//    Assert.assertEquals("洗车", itemIndexDTO1.getItemName());
//    Assert.assertEquals(ItemTypes.WASH, itemIndexDTO1.getItemType());
//    Assert.assertEquals(memberDTO.getId(), itemIndexDTO1.getMemberCardId());
//    Assert.assertEquals(washBeautyOrderDTO.getId(), itemIndexDTO1.getOrderId());
//    Assert.assertEquals(washBeautyOrderDTO.getTotal(), itemIndexDTO1.getOrderTotalAmount(), 0.001);
//    Assert.assertEquals(OrderTypes.WASH_BEAUTY, itemIndexDTO1.getOrderType());
//    Assert.assertEquals(shopId, itemIndexDTO1.getShopId());
//    Assert.assertEquals(washBeautyOrderDTO.getLicenceNo(), itemIndexDTO1.getVehicle());
//    Assert.assertEquals(1, itemIndexDTO1.getIncreasedTimes().intValue());
//    Assert.assertEquals(OrderStatus.WASH_SETTLED, itemIndexDTO2.getOrderStatus());
//    Assert.assertEquals(washBeautyOrderDTO.getCustomerId(), itemIndexDTO2.getCustomerId());
//    Assert.assertEquals(washBeautyOrderDTO.getCustomer(), itemIndexDTO2.getCustomerOrSupplierName());
//    Assert.assertEquals(washBeautyOrderItemDTO2.getPrice(), itemIndexDTO2.getItemPrice(), 0.001);
//    Assert.assertEquals(0, itemIndexDTO2.getItemCostPrice(), 0.001);
//    Assert.assertEquals(washBeautyOrderItemDTO2.getId(), itemIndexDTO2.getItemId());
//    Assert.assertEquals(washBeautyOrderItemDTO2.getServiceId(), itemIndexDTO2.getServiceId());
//    Assert.assertEquals("打蜡", itemIndexDTO2.getItemName());
//    Assert.assertEquals(ItemTypes.WASH, itemIndexDTO2.getItemType());
//    Assert.assertEquals(memberDTO.getId(), itemIndexDTO2.getMemberCardId());
//    Assert.assertEquals(washBeautyOrderDTO.getId(), itemIndexDTO2.getOrderId());
//    Assert.assertEquals(washBeautyOrderDTO.getTotal(), itemIndexDTO2.getOrderTotalAmount(), 0.001);
//    Assert.assertEquals(OrderTypes.WASH_BEAUTY, itemIndexDTO2.getOrderType());
//    Assert.assertEquals(shopId, itemIndexDTO2.getShopId());
//    Assert.assertEquals(washBeautyOrderDTO.getLicenceNo(), itemIndexDTO2.getVehicle());
//    Assert.assertEquals(1, itemIndexDTO2.getIncreasedTimes().intValue());
  }


  /**
   * 新会员购卡
   *
   * @throws Exception
   */
  @Test
  public void testSaveMemberCardOrderByNewMember() throws Exception {
    ITxnService txnService = ServiceManager.getService(ITxnService.class);
    ISearchService searchService = ServiceManager.getService(ISearchService.class);
    IMembersService membersService = ServiceManager.getService(IMembersService.class);

    Long shopId = createShop();
    request.getSession().setAttribute("shopId", shopId);
    CustomerRecordDTO customerRecordDTO = new CustomerRecordDTO();
    customerRecordDTO.setName("伟哥一号准备");
    customerRecordDTO.setInvoiceCategory("1111");
    customerRecordDTO.setSettlementType("1111");
    customerController.addCustomer(request, response, modelMap, customerRecordDTO);
    Long customerId = (Long) request.getSession().getAttribute("customerId");

    MemberCardDTO memberCardDTO = createMemberCard(shopId, "会员卡一号准备");

    List<MemberCardOrderServiceDTO> memberCardOrderServiceDTOs = memberController.combineOldServiceAndNewService(null, memberCardDTO);

    for (MemberCardOrderServiceDTO memberCardOrderServiceDTO : memberCardOrderServiceDTOs) {
      if (-1 == memberCardOrderServiceDTO.getBalanceTimes()) {
        memberCardOrderServiceDTO.setTimesStatus(Integer.valueOf(TimesStatus.UNLIMITED.getStatus()).intValue());
      }
      if (-1 == memberCardOrderServiceDTO.getDeadline()) {
        memberCardOrderServiceDTO.setDeadlineStatus(1);
      } else {
        memberCardOrderServiceDTO.setDeadlineStr(DateUtil.convertDateLongToDateString(DateUtil.YEAR_MONTH_DATE,
            memberCardOrderServiceDTO.getDeadline()));
      }
    }

    MemberCardOrderDTO memberCardOrderDTO = new MemberCardOrderDTO();
    memberCardOrderDTO.setMemberCardOrderServiceDTOs(memberCardOrderServiceDTOs);
    ReceivableDTO receivableDTO = new ReceivableDTO();
    ReceptionRecordDTO[] receptionRecordDTOs = new ReceptionRecordDTO[1];
    ReceptionRecordDTO receptionRecordDTO = new ReceptionRecordDTO();
    receptionRecordDTOs[0] = receptionRecordDTO;
    receivableDTO.setTotal(2000);
    receivableDTO.setDiscount(200);
    receivableDTO.setDebt(200);
    receivableDTO.setCash(800d);
    receivableDTO.setBankCard(300d);
    receivableDTO.setCheque(500d);
    receptionRecordDTOs[0].setChequeNo("555555");
    receivableDTO.setRecordDTOs(receptionRecordDTOs);

    List<MemberCardOrderItemDTO> memberCardOrderItemDTOs = new ArrayList<MemberCardOrderItemDTO>();
    MemberCardOrderItemDTO memberCardOrderItemDTO = new MemberCardOrderItemDTO();
    memberCardOrderItemDTO.setPrice(2000.0);
    memberCardOrderItemDTO.setAmount(1800.0);
    memberCardOrderItemDTO.setWorth(2500.0);
    memberCardOrderItemDTO.setCardId(memberCardDTO.getId());
    memberCardOrderDTO.setMemberCardOrderItemDTOs(memberCardOrderItemDTOs);
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setMemberNo("123456");
    memberDTO.setPassword("000000");
    memberDTO.setBalance(2500.0);
    memberCardOrderDTO.setReceivableDTO(receivableDTO);
    memberCardOrderDTO.setCustomerId(customerId);
    memberCardOrderDTO.setExecutorId(0L);
    memberCardOrderDTO.setShopId(shopId);
    memberCardOrderDTO.setCustomerName(customerRecordDTO.getName());
    memberCardOrderDTO.setRepayTime("2012-09-20");
    memberCardOrderDTO.setTotal(2000.0);
    memberCardOrderDTO.setMobile("12345678987");
    memberCardOrderDTO.setMemberDTO(memberDTO);

    StringBuffer sb = new StringBuffer("");
    //txn事务保存成功标志
    boolean txnTransSuccess = false;
    //user事务保存成功标志
    boolean userTransSuccess = false;
    //search事务保存成功标志
    boolean searchTransSuccess = false;

    Long vestDate = System.currentTimeMillis();
    String vestDateStr = DateUtil.dateLongToStr(vestDate, DateUtil.DATE_STRING_FORMAT_DAY_HOUR_MIN);
    if (null != memberCardOrderDTO) {
      memberCardOrderDTO.setVestDate(vestDate);
      memberCardOrderDTO.setVestDateStr(vestDateStr);
      memberCardOrderDTO.setEditDate(vestDate);
      memberCardOrderDTO.setEditDateStr(vestDateStr);
      memberCardOrderDTO.setShopId(shopId);
      memberCardOrderDTO.setTotal(memberCardOrderDTO.getReceivableDTO().getTotal());

      //保存membercardorder,membercardorderitem,membercardorderservice,receivable,receptionrecord,debt表
      memberCardOrderDTO = txnService.saveMemberCardOrder(memberCardOrderDTO);
      //txnTransSuccess保存成功
      txnTransSuccess = true;
      //保存更新会员信息member和memberservice,customer,customerrecord
      membersService.saveOrUpdateMember(memberCardOrderDTO);
      //userTransSuccess保存成功
      userTransSuccess = true;
      //保存orderindex和itemindex。和membercardorderservice也要保存到itemindex中
//      searchService.saveOrderIndexAndItemIndexOfMemberCardOrder(memberCardOrderDTO); todo merge
      //searchTransSuccess保存成功
      searchTransSuccess = true;
    }

    if (userTransSuccess) {
      sb.append("success");
    } else {
      sb.append("error");
    }
    String jsonStr = sb.toString();
    Map<String, String> jsonMap = new HashMap();
    jsonMap.put("resu", jsonStr);
    jsonMap.put("orderId", memberCardOrderDTO.getId().toString());
    response.setCharacterEncoding("UTF-8");

    jsonStr = JsonUtil.mapToJson(jsonMap);
    modelMap.addAttribute("memberCardOrderDTO", memberCardOrderDTO);
    modelMap.addAttribute("searchTransSuccess", searchTransSuccess);
    modelMap.addAttribute("jsonStr", jsonStr);

    orderRunBusinessStat(memberCardOrderDTO);


    jsonStr = (String) modelMap.get("jsonStr");
    searchTransSuccess = ((Boolean) modelMap.get("searchTransSuccess")).booleanValue();
    MemberCardOrderDTO newMemberCardOrderDTO = (MemberCardOrderDTO) modelMap.get("memberCardOrderDTO");
    String expectedJsonStr = "{\"resu\":\"success\",\"orderId\":\"" + newMemberCardOrderDTO.getId() + "\"}";
    Assert.assertEquals(true, searchTransSuccess);
    Assert.assertEquals(expectedJsonStr, jsonStr);
    Assert.assertEquals(customerId, newMemberCardOrderDTO.getCustomerId());
    Assert.assertEquals(true, null == newMemberCardOrderDTO.getId() ? false : true);

    MemberDTO newMemberDTO = membersService.getMemberByCustomerId(newMemberCardOrderDTO.getShopId(), customerId);
    Assert.assertEquals(memberDTO.getMemberNo(), newMemberDTO.getMemberNo());
    Assert.assertEquals(Double.valueOf(2500), newMemberDTO.getBalance());
    for (MemberServiceDTO memberServiceDTO : newMemberDTO.getMemberServiceDTOs()) {
      ServiceDTO serviceDTO = ServiceManager.getService(ITxnService.class).getServiceById(memberServiceDTO.getServiceId());
      if ("换机油".equals(serviceDTO.getName())) {
        Assert.assertEquals(new Integer(8), memberServiceDTO.getTimes());
        Assert.assertEquals(DateUtil.getDeadline(System.currentTimeMillis(), 5), memberServiceDTO.getDeadline());
      } else {
        Assert.assertEquals(new Integer(-1), memberServiceDTO.getTimes());
        Assert.assertEquals(DateUtil.getDeadline(System.currentTimeMillis(), 2), memberServiceDTO.getDeadline());
      }
    }
  }

  /**
   * 老会员续卡
   *
   * @throws Exception
   */
  @Test
  public void testSaveMemberCardOrderByOldMember() throws Exception {
    ITxnService txnService = ServiceManager.getService(ITxnService.class);
    ISearchService searchService = ServiceManager.getService(ISearchService.class);
    IMembersService membersService = ServiceManager.getService(IMembersService.class);

    Long shopId = createShop();
    request.getSession().setAttribute("shopId", shopId);

    //创建客户
    CustomerRecordDTO customerRecordDTO = new CustomerRecordDTO();
    customerRecordDTO.setName("伟哥一号准备");
    customerRecordDTO.setInvoiceCategory("1111");
    customerRecordDTO.setSettlementType("1111");
    customerController.addCustomer(request, response, modelMap, customerRecordDTO);
    Long customerId = (Long) request.getSession().getAttribute("customerId");

    //创建会员
    MemberDTO memberDTO = createMember(shopId, customerId, "会员一号准备", "123456");

    Map<Long, MemberServiceDTO> memberServiceDTOMap = MemberServiceDTO.listToMap(memberDTO.getMemberServiceDTOs());
    //创建套餐卡
    MemberCardDTO memberCardDTO = new MemberCardDTO();
    memberCardDTO.setPrice(2000.0);
    memberCardDTO.setWorth(2500.0);
    memberCardDTO.setPercentageAmount(10.0);
    memberCardDTO.setName("会员卡一号准备");
    memberCardDTO.setShopId(shopId);
    List<MemberCardServiceDTO> memberCardServiceDTOs = new ArrayList<MemberCardServiceDTO>();
    MemberCardServiceDTO memberCardServiceDTO = null;
    //创建和会员卡上服务种类相同的服务
    for (MemberServiceDTO memberServiceDTO : memberDTO.getMemberServiceDTOs()) {
      if ("施工项目1".equals(memberServiceDTO.getServiceName())) {
        memberCardServiceDTO = new MemberCardServiceDTO();
        memberCardServiceDTO.setServiceId(memberServiceDTO.getServiceId());
        memberCardServiceDTO.setServiceName("施工项目1");
        memberCardServiceDTO.setTerm(5);
        memberCardServiceDTO.setTimes(-1);
        memberCardServiceDTOs.add(memberCardServiceDTO);
      } else {
        memberCardServiceDTO = new MemberCardServiceDTO();
        memberCardServiceDTO.setServiceId(memberServiceDTO.getServiceId());
        memberCardServiceDTO.setServiceName("施工项目2");
        memberCardServiceDTO.setTerm(2);
        memberCardServiceDTO.setTimes(8);
        memberCardServiceDTOs.add(memberCardServiceDTO);
      }
    }

    //创建会员卡上没有的服务
    memberCardServiceDTO = new MemberCardServiceDTO();
    memberCardServiceDTO.setServiceId(Long.valueOf(3));
    memberCardServiceDTO.setServiceName("项目施工3");
    memberCardServiceDTO.setTerm(2);
    memberCardServiceDTO.setTimes(9);
    memberCardServiceDTOs.add(memberCardServiceDTO);
    Map<Long, MemberCardServiceDTO> memberCardServiceDTOMap = MemberCardServiceDTO.listToMap(memberCardServiceDTOs);
    memberCardDTO.setMemberCardServiceDTOs(memberCardServiceDTOs);

    memberCardDTO = membersService.saveOrUpdateMemberCard(memberCardDTO);

    //合并会员卡上的服务和套餐上的服务
    List<MemberCardOrderServiceDTO> memberCardOrderServiceDTOs = memberController.combineOldServiceAndNewService(memberDTO, memberCardDTO);

    //根据次数和期限给次数和期限的状态赋值，这两个都是从页面传过来的，这里模拟一下
    for (MemberCardOrderServiceDTO memberCardOrderServiceDTO : memberCardOrderServiceDTOs) {
      if (-1 == memberCardOrderServiceDTO.getBalanceTimes()) {
        //1表示无限次的状态
        memberCardOrderServiceDTO.setTimesStatus(Integer.valueOf(TimesStatus.UNLIMITED.getStatus()).intValue());
      }
      if (-1 == memberCardOrderServiceDTO.getDeadline()) {
        //1表示无限期的状态
        memberCardOrderServiceDTO.setDeadlineStatus(1);
      } else {
        memberCardOrderServiceDTO.setDeadlineStr(DateUtil.convertDateLongToDateString(DateUtil.YEAR_MONTH_DATE,
            memberCardOrderServiceDTO.getDeadline()));
      }
    }

    //整理从页面传过来的数据，封装到memberCardOrderDTO中
    MemberCardOrderDTO memberCardOrderDTO = new MemberCardOrderDTO();
    memberCardOrderDTO.setMemberCardOrderServiceDTOs(memberCardOrderServiceDTOs);
    ReceivableDTO receivableDTO = new ReceivableDTO();
    ReceptionRecordDTO[] receptionRecordDTOs = new ReceptionRecordDTO[1];
    ReceptionRecordDTO receptionRecordDTO = new ReceptionRecordDTO();
    receptionRecordDTOs[0] = receptionRecordDTO;
    receivableDTO.setTotal(2000);
    receivableDTO.setDiscount(200);
    receivableDTO.setDebt(200);
    receivableDTO.setCash(800d);
    receivableDTO.setBankCard(300d);
    receivableDTO.setCheque(500d);
    receptionRecordDTOs[0].setChequeNo("555555");
    receivableDTO.setRecordDTOs(receptionRecordDTOs);

    List<MemberCardOrderItemDTO> memberCardOrderItemDTOs = new ArrayList<MemberCardOrderItemDTO>();
    MemberCardOrderItemDTO memberCardOrderItemDTO = new MemberCardOrderItemDTO();
    memberCardOrderItemDTO.setPrice(2000.0);
    memberCardOrderItemDTO.setAmount(1800.0);
    memberCardOrderItemDTO.setWorth(2500.0);
    memberCardOrderItemDTO.setCardId(memberCardDTO.getId());
    memberCardOrderDTO.setMemberCardOrderItemDTOs(memberCardOrderItemDTOs);

    memberDTO.setBalance(2500.0 + (null == memberDTO.getBalance() ? 0.0 : memberDTO.getBalance().doubleValue()));
    memberCardOrderDTO.setReceivableDTO(receivableDTO);
    memberCardOrderDTO.setCustomerId(customerId);
    memberCardOrderDTO.setExecutorId(0L);
    memberCardOrderDTO.setShopId(shopId);
    memberCardOrderDTO.setCustomerName(customerRecordDTO.getName());
    memberCardOrderDTO.setRepayTime("2012-09-20");
    memberCardOrderDTO.setTotal(2000.0);
    memberCardOrderDTO.setMobile("12345678987");
    memberCardOrderDTO.setMemberDTO(memberDTO);

    //够卡controller
    // memberController.saveMemberCardOrder(modelMap, request, response, memberCardOrderDTO);

    PrintWriter out = response.getWriter();
    StringBuffer sb = new StringBuffer("");
    //txn事务保存成功标志
    boolean txnTransSuccess = false;
    //user事务保存成功标志
    boolean userTransSuccess = false;
    //search事务保存成功标志
    boolean searchTransSuccess = false;

    Long vestDate = System.currentTimeMillis();
    String vestDateStr = DateUtil.dateLongToStr(vestDate, DateUtil.DATE_STRING_FORMAT_DAY_HOUR_MIN);
    if (null != memberCardOrderDTO) {
      memberCardOrderDTO.setVestDate(vestDate);
      memberCardOrderDTO.setVestDateStr(vestDateStr);
      memberCardOrderDTO.setEditDate(vestDate);
      memberCardOrderDTO.setEditDateStr(vestDateStr);
      memberCardOrderDTO.setShopId(shopId);
      memberCardOrderDTO.setTotal(memberCardOrderDTO.getReceivableDTO().getTotal());

      //保存membercardorder,membercardorderitem,membercardorderservice,receivable,receptionrecord,debt表
      memberCardOrderDTO = txnService.saveMemberCardOrder(memberCardOrderDTO);
      //txnTransSuccess保存成功
      txnTransSuccess = true;
      //保存更新会员信息member和memberservice,customer,customerrecord
      membersService.saveOrUpdateMember(memberCardOrderDTO);
      //userTransSuccess保存成功
      userTransSuccess = true;
      //保存orderindex和itemindex。和membercardorderservice也要保存到itemindex中
      searchService.saveOrderIndexAndItemIndexOfMemberCardOrder(memberCardOrderDTO);
      //searchTransSuccess保存成功
      searchTransSuccess = true;
    }

    if (userTransSuccess) {
      sb.append("success");
    } else {
      sb.append("error");
    }
    String jsonStr = sb.toString();
    Map<String, String> jsonMap = new HashMap();
    jsonMap.put("resu", jsonStr);
    jsonMap.put("orderId", memberCardOrderDTO.getId().toString());
    response.setCharacterEncoding("UTF-8");

    jsonStr = JsonUtil.mapToJson(jsonMap);
    modelMap.addAttribute("memberCardOrderDTO", memberCardOrderDTO);
    modelMap.addAttribute("searchTransSuccess", searchTransSuccess);
    modelMap.addAttribute("jsonStr", jsonStr);


    orderRunBusinessStat(memberCardOrderDTO);

    jsonStr = (String) modelMap.get("jsonStr");
    searchTransSuccess = ((Boolean) modelMap.get("searchTransSuccess")).booleanValue();
    MemberCardOrderDTO newMemberCardOrderDTO = (MemberCardOrderDTO) modelMap.get("memberCardOrderDTO");

    String expectedJsonStr = "{\"resu\":\"success\",\"orderId\":\"" + newMemberCardOrderDTO.getId() + "\"}";

    Assert.assertEquals(true, searchTransSuccess);
    Assert.assertEquals(expectedJsonStr, jsonStr);
    Assert.assertEquals(customerId, newMemberCardOrderDTO.getCustomerId());
    Assert.assertEquals(true, null == newMemberCardOrderDTO.getId() ? false : true);

    MemberDTO newMemberDTO = membersService.getMemberByCustomerId(newMemberCardOrderDTO.getShopId(), customerId);
    Assert.assertEquals(memberDTO.getMemberNo(), newMemberDTO.getMemberNo());
    Assert.assertEquals(memberDTO.getBalance(), newMemberDTO.getBalance());

    txnService = ServiceManager.getService(TxnService.class);
    ReceivableDTO newReceivableDTO = txnService.getReceivableByShopIdAndOrderTypeAndOrderId(shopId, null, newMemberCardOrderDTO.getId());

    Assert.assertEquals(2000.0, newReceivableDTO.getTotal());
    Assert.assertEquals(200.0, newReceivableDTO.getDiscount());
    Assert.assertEquals(200.0, newReceivableDTO.getDebt());
    Assert.assertEquals(800.0, newReceivableDTO.getCash());
    Assert.assertEquals(300.0, newReceivableDTO.getBankCard());
    Assert.assertEquals(500.0, newReceivableDTO.getCheque());

    for (MemberServiceDTO memberServiceDTO : newMemberDTO.getMemberServiceDTOs()) {
      MemberCardServiceDTO memberCardServiceDTOTest = memberCardServiceDTOMap.get(memberServiceDTO.getServiceId());
      if ("施工项目1".equals(memberCardServiceDTOTest.getServiceName())) {
        Assert.assertEquals(new Integer(-1), memberServiceDTO.getTimes());
        Assert.assertEquals(DateUtil.getDeadline(null == memberServiceDTOMap.get(memberServiceDTO.getServiceId()) ? null : memberServiceDTOMap.get(memberServiceDTO.getServiceId()).getDeadline(), 5),
            memberServiceDTO.getDeadline());
      } else if ("施工项目2".equals(memberCardServiceDTOTest.getServiceName())) {
        Assert.assertEquals(new Integer(8), memberServiceDTO.getTimes());
        Assert.assertEquals(DateUtil.getDeadline(null == memberServiceDTOMap.get(memberServiceDTO.getServiceId()) ? null : memberServiceDTOMap.get(memberServiceDTO.getServiceId()).getDeadline(), 2), memberServiceDTO.getDeadline());
      } else {
        Assert.assertEquals(new Integer(9), memberServiceDTO.getTimes());
        Assert.assertEquals(DateUtil.getDeadline(null == memberServiceDTOMap.get(memberServiceDTO.getServiceId()) ? null : memberServiceDTOMap.get(memberServiceDTO.getServiceId()).getDeadline(), 2), memberServiceDTO.getDeadline());
      }
    }
  }

}



