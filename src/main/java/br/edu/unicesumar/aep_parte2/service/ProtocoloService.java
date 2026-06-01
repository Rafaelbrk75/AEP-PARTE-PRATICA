package br.edu.unicesumar.aep_parte2.service;

import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProtocoloService {

    // AtomicLong garante que dois requests simultâneos
    // nunca gerem o mesmo número
    private final AtomicLong contador = new AtomicLong(0);

    public String gerar() {
        long numero = contador.incrementAndGet();
        return String.format("OBS-%d-%06d", Year.now().getValue(), numero);
        // ex: OBS-2026-000001
    }
}