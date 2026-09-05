package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;

  /**
   * 주문
   */
  @Transactional
  public Order order(Long memberId, Long itemId, int count) {
    // 동일 라이프사이클 (Transaction)에 묶어서 처리할 경우에 사용 :: cascade = CascadeType.ALL

    // 엔티티 조회
    Member member = memberRepository.findOne(memberId);
    // 주문 상품
    Item item = itemRepository.findOne(itemId);

    // 배송정보
    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());

    // 주문 명세 / 주문 생성
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
    Order order = Order.createOrder(member, delivery, orderItem);

    // 주문 저장
    return orderRepository.save(order);
  }

  // 취소
  @Transactional
  public Order cancelOrder(Long orderId) {
    // 주문 엔티티
    Order order = orderRepository.findOne(orderId);
    // 주문 취소
    order.cancel();
    return order;
  }

  // 검색
//  public List<Order> findOrders(OrderSearch orderSearch) {
//    return orderRepository.findAll();
//  }
}
