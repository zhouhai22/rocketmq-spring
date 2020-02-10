/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.spring.support;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpringBeanUtil {

    /**
     * Override applicationContext.getBeansWithAnnotation method to make sure without same ProxyTarget beans
     *
     * @param applicationContext spring Application Context
     * @param clazz              annotation class
     * @return beans map without proxyTarget bean
     */
    public static Map<String, Object> getBeansWithAnnotation(@NonNull ConfigurableApplicationContext applicationContext, Class<? extends Annotation> clazz) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(clazz);
        Map<String, Object> filterBeans = new HashMap<>(beans.size());
        // remove proxy target
        Set<Map.Entry<String, Object>> entrySet = beans.entrySet();
        entrySet.forEach((entry) -> {
            final String beanName = entry.getKey();
            if (!ScopedProxyUtils.isScopedTarget(beanName)) {
                filterBeans.put(beanName, entry.getValue());
            }
        });
        return filterBeans;
    }

}