package com.futuro.api_iot_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

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
    public DefaultJmsListenerContainerFactory jmsTopicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setErrorHandler(new CustomJmsErrorHandler());
        
        factory.setMessageConverter(new SimpleMessageConverter() {
            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                return message;
            }
        });
        
        return factory;
    }

}
