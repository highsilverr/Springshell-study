package com.example.demo.price.formatter;

import com.example.demo.price.dto.Price;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("eng")
public class EnglishOutputFormatter implements OutPutFormatter{

    @Override
    public String format(Price price, int usage) {
        int total = price.getUnitPrice() * usage;
        return String.format("city: %s, Sector: %s, unit price(won) : %d, bill total(won): %d",
                price.getCity(),price.getSector(), price.getUnitPrice(), total);
    }

}
