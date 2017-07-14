package shopping.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product
 * Created by claudio on 13/07/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    String id;

    String name;

}
