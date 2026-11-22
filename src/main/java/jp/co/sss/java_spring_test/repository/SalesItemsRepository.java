package jp.co.sss.java_spring_test.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.SalesItems;

public interface SalesItemsRepository extends JpaRepository<SalesItems, Integer> {
	
	// 現在日時がセール期間内（startMonth <= now かつ endMonth >= now）のセール品を全て取得
    List<SalesItems> findByStartMonthLessThanEqualAndEndMonthGreaterThanEqual(Timestamp now1, Timestamp now2);
    
    // 指定した商品IDかつ、現在日時がセール期間内のセール情報を1件取得する
    SalesItems findByProduct_ProductIdAndStartMonthLessThanEqualAndEndMonthGreaterThanEqual(Integer productId, Timestamp now1, Timestamp now2);

}
