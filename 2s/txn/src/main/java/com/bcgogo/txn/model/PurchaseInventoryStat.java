package com.bcgogo.txn.model;

import com.bcgogo.model.LongIdentifier;
import com.bcgogo.stat.dto.PurchaseInventoryStatDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 采购成本统计
 * User: Jimuchen
 * Date: 12-10-24
 * Time: 下午4:36
 */
@Entity
@Table(name="purchase_inventory_stat")
public class PurchaseInventoryStat extends LongIdentifier {

  private Long shopId;
  private Long productId;
  private Integer statYear;
  private Integer statMonth;
  private Integer statDay;
  private double amount;    //数量
  private int times;        //次数
  private double total;     //总额

  private Long statTime;//每次统计的时间

  @Column(name="shop_id")
  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }

  @Column(name="product_id")
  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  @Column(name="stat_year")
  public Integer getStatYear() {
    return statYear;
  }

  public void setStatYear(Integer statYear) {
    this.statYear = statYear;
  }

  @Column(name="stat_month")
  public Integer getStatMonth() {
    return statMonth;
  }

  public void setStatMonth(Integer statMonth) {
    this.statMonth = statMonth;
  }

  @Column(name="stat_day")
  public Integer getStatDay() {
    return statDay;
  }

  public void setStatDay(Integer statDay) {
    this.statDay = statDay;
  }

  @Column(name="amount")
  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  @Column(name="times")
  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  @Column(name="total")
  public double getTotal() {
    return total;
  }

  public void setTotal(double total) {
    this.total = total;
  }

  @Column(name="stat_time")
  public Long getStatTime() {
    return statTime;
  }

  public void setStatTime(Long statTime) {
    this.statTime = statTime;
  }

  public PurchaseInventoryStatDTO toDTO() {
    PurchaseInventoryStatDTO inventoryStatDTO = new PurchaseInventoryStatDTO();
    inventoryStatDTO.setShopId(getShopId());
    inventoryStatDTO.setProductId(getProductId());
    inventoryStatDTO.setAmount(getAmount());
    inventoryStatDTO.setTimes(getTimes());
    inventoryStatDTO.setTotal(getTotal());
    inventoryStatDTO.setStatDay(getStatDay());
    inventoryStatDTO.setStatMonth(getStatMonth());
    inventoryStatDTO.setStatYear(getStatYear());
    return inventoryStatDTO;
  }
}