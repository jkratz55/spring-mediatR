package io.github.jkratz55.mediator.mock

import io.github.jkratz55.mediator.core.Request
import io.github.jkratz55.mediator.core.RequestHandler
import java.math.BigDecimal

data class CalculateOrderTotalRequest(val price: BigDecimal,
                                 val qty: BigDecimal): Request<BigDecimal>

class CalculateOrderTotalRequestHandler: RequestHandler<CalculateOrderTotalRequest, BigDecimal> {

    override fun handle(request: CalculateOrderTotalRequest): BigDecimal {
        return request.price.multiply(request.qty)
    }
}

