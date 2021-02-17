package winterschoolone;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

import winterschoolone.external.Coupon;
import winterschoolone.external.CouponService;

import java.util.List;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String userId;
    private String menuId;
    private Integer qty;
    private String cancelYn;
    private String useCouponYN;

    @PostPersist
    public void onPostPersist(){
        Payed payed = new Payed();
        BeanUtils.copyProperties(this, payed);
        payed.publishAfterCommit();
        
        try {
                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        
        if("Y".equals(this.getUseCouponYN()) && this.getQty()==1) {//coupon 사용(1잔만 적용 가능)
	        Coupon coupon = new Coupon();
	        coupon.setOrderId(this.getId());
	        coupon.setMenuId(this.menuId);
	        coupon.setQty(this.getQty());
	        coupon.setUserId(this.getUserId());
	        // mappings goes here
	        PaymentApplication.applicationContext.getBean(CouponService.class)
	        .use(coupon);
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        Refunded refunded = new Refunded();
        BeanUtils.copyProperties(this, refunded);
        refunded.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getCancelYn() {
        return cancelYn;
    }

    public void setCancelYn(String cancelYn) {
        this.cancelYn = cancelYn;
    }

    public String getUseCouponYN() {
        return useCouponYN;
    }

    public void setUseCouponYN(String useCouponYN) {
        this. useCouponYN = useCouponYN;
    }



}
