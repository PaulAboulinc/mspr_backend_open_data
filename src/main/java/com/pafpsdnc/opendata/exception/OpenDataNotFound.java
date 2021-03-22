package com.pafpsdnc.opendata.exception;

import javassist.NotFoundException;

public class OpenDataNotFound extends NotFoundException {
    public OpenDataNotFound(String msg) {
        super(msg);
    }

    public OpenDataNotFound() {
        super("Les données n'ont pas été trouvées");
    }
}
