package com.example.AirplaneBooking.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.AirplaneBooking.dto.booking.BookingDTO;
import com.example.AirplaneBooking.dto.flight.FlightDTO;
import com.example.AirplaneBooking.model.entity.Booking;
import com.example.AirplaneBooking.model.entity.Flight;
import com.example.AirplaneBooking.model.entity.Passenger;
import com.example.AirplaneBooking.dto.passenger.PassengerDTO;

import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.config.Configuration.AccessLevel;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);

        // Cấu hình mapping cụ thể cho Flight -> FlightDTO
        modelMapper.createTypeMap(Flight.class, FlightDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getFlightId(), FlightDTO::setFlightId);
                    mapper.map(src -> src.getFlightCode(), FlightDTO::setFlightCode);
                    mapper.map(src -> src.getAircraft().getAircraftId(), FlightDTO::setAircraftId);
                    mapper.map(src -> src.getDepartureAirport().getAirportId(), FlightDTO::setDepartureAirportId);
                    mapper.map(src -> src.getArrivalAirport().getAirportId(), FlightDTO::setArrivalAirportId);
                    mapper.map(src -> src.getDepartureDateTime(), FlightDTO::setDepartureDateTime);
                    mapper.map(src -> src.getArrivalDateTime(), FlightDTO::setArrivalDateTime);
                    mapper.map(src -> src.getBaseFare(), FlightDTO::setBaseFare);
                });

        // Add explicit mapping for Passenger to PassengerDTO
        modelMapper.createTypeMap(Passenger.class, PassengerDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Passenger::getFullName, PassengerDTO::setFullName);
                    // Add other explicit mappings if needed
                });
        modelMapper.createTypeMap(Booking.class, BookingDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getFlight().getFlightId(), BookingDTO::setFlightId);
                });

        return modelMapper;
    }
}