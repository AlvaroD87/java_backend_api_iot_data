package com.futuro.api_iot_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jakarta.jms.ConnectionFactory;

/**
 * Proporciona las configuraciones para conectarse a una cola de mensajería
 */
@Configuration
@EnableJms
public class JmsConfig {

    /**
     * Entrega una capa de personalización para las configuraciones a la cola de mensajería
     * @param connectionFactory - Elemento base de personalización de la conexión.
     * @return
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("5-10");
        
        factory.setErrorHandler(new CustomJmsErrorHandler());
        return factory;
    }
}
