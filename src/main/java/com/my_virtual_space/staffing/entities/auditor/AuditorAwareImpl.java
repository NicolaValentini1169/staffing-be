package com.my_virtual_space.staffing.entities.auditor;

import com.my_virtual_space.staffing.security.utils.AuthenticationUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Classe di utilità per la gestione del settaggio del utente durante una creazione o modifica
 */

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(AuthenticationUtils.getUserStringId());
    }
}
