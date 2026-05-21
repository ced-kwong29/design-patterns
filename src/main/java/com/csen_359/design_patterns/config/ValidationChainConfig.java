package com.csen_359.design_patterns.config;

import com.csen_359.design_patterns.repository.UsageEntryRepository;
import com.csen_359.design_patterns.validation.CategoryValidationHandler;
import com.csen_359.design_patterns.validation.DuplicateCheckHandler;
import com.csen_359.design_patterns.validation.RangeValidationHandler;
import com.csen_359.design_patterns.validation.UsageEntryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Assembles the Chain of Responsibility validation pipeline.
 *
 * <p>Order is defined here and nowhere else - reorder or insert handlers by
 * editing this single method, with no change to handler code:
 *
 * <pre>
 *   RangeValidationHandler -&gt; CategoryValidationHandler -&gt; DuplicateCheckHandler
 * </pre>
 *
 * The head of the chain is exposed as a {@link UsageEntryHandler} bean that
 * {@code UsageService} injects and invokes.
 */
@Configuration
public class ValidationChainConfig {

    @Bean
    public UsageEntryHandler usageValidationChain(UsageEntryRepository usageEntryRepository) {
        UsageEntryHandler range = new RangeValidationHandler();
        UsageEntryHandler category = new CategoryValidationHandler();
        UsageEntryHandler duplicate = new DuplicateCheckHandler(usageEntryRepository);

        range.linkTo(category).linkTo(duplicate);
        return range;
    }
}
