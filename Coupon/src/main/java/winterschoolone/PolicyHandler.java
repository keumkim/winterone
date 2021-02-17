package winterschoolone;

import winterschoolone.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }
    
    @Autowired
    CouponRepository couponRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAssigned_(@Payload Assigned assigned){ //stampQty 증가
    	
    	if(assigned.isMe()){
        	Optional<Coupon> optional = couponRepository.findById(assigned.getOrderId());
        	if(optional != null && optional.isPresent())
        	{
        		Coupon coupon = optional.get();
        		
        		// coupon 생성을 위한 stamp 개수 증가
        		coupon.setStampQty(assigned.getQty());
        		
        		while(true) {
        			if(coupon.getStampQty()>=10) { //10개 이상일 경우 Coupon지급
        				coupon.setCouponQty(+1);
        				coupon.setStampQty(-10);
        			}
        			else { 
        				break;
        			}			
        		}
        		
        		couponRepository.save(coupon);
        	}
            
            System.out.println("##### listener  : " + assigned.toJson());
        }
    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayed_(@Payload Payed payed){ //쿠폰 사용 처리

    	if(payed.isMe()){
            System.out.println("##### listener  : " + payed.toJson());
            
            Coupon coupon = new Coupon();
            coupon.setMenuId(payed.getMenuId());
            coupon.setOrderId(payed.getOrderId());
            coupon.setQty(payed.getQty());
            coupon.setUserId(payed.getUserId());
  
            if("Y".equals(payed.getUseCouponYN())) {
            	coupon.setCouponQty(-1);
            }
            
            couponRepository.save(coupon);
        }
    	
    }
  
}
