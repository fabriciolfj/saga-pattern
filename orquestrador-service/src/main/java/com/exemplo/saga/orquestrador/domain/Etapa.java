package com.exemplo.saga.orquestrador.domain;


import com.exemplo.saga.orquestrador.util.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Etapa {

    INICIALIZACAO(Constants.DESC_INICILIZACAO) {
        public Etapa next() {
            return FRAUDE;
        }
    },

    FRAUDE(Constants.DESC_FRAUDE) {
        public Etapa next(boolean isFraude) {
            return isFraude ? CANCELAMENTO : DEPOSITO;
        }
    },

    CANCELAMENTO(Constants.DESC_CANCELAMENTO) {
        public Etapa next() {
            throw new IllegalArgumentException("this step " + this.name() + " is last");
        }
    },

    DEPOSITO(Constants.DESC_DEPOSITO) {
        public Etapa next() {
            throw new IllegalArgumentException("this step " + this.name() + " is last");
        }
    };

    public Etapa next() {
        throw new IllegalStateException("step " + name() + " needs the fraud result to advance");
    }

    public Etapa next(final boolean isFraude) {
        throw new IllegalStateException("step " + name() + " does not depend on the fraud result");
    }

    private final String descricao;
}
