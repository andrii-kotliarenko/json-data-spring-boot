/*
 * Copyright (C) 2020 Anton Hrytsenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hrytsenko.jsondata.springboot.web;

import com.github.hrytsenko.jsondata.JsonEntity;
import com.github.hrytsenko.jsondata.JsonValidatorException;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Aspect
@Order(0)
@Configuration
@AllArgsConstructor
class ValidateRequestAspect {

    ValidatorSource validatorSource;

    @Before("@annotation(config)")
    public void handle(JoinPoint point, ValidateRequest config) {
        JsonEntity<?> target = (JsonEntity<?>) point.getArgs()[0];

        String schemaName = config.value();
        try {
            validatorSource.getValidator(schemaName)
                    .validate(target);
        } catch (JsonValidatorException exception) {
            throw new ValidateRequestException("Invalid request " + schemaName, exception);
        }
    }

}
