package com.exemplo.saga.orquestrador.domain;


public enum Etapa {

    INICIALIZACAO {
        public Etapa next() {
            return FRAUDE;
        }
    },

    FRAUDE {
        public Etapa next(boolean isFraude) {
            return isFraude ? CANCELAMENTO : DEPOSITO;
        }
    },

    CANCELAMENTO {
        public Etapa next() {
            throw new IllegalArgumentException("this step " + this.name() + " is last");
        }
    },

    DEPOSITO {
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
}
