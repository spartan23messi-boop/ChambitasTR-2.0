package com.leonardo.escuela.services;

import java.util.List;

public interface CrudServices <RQ, RS> {

    List<RS> Listar();
    RS obtenerPorId(Long id);

    RS registro(RQ request);

    RS actualizar(RQ request, Long id);

    void eliminar(Long id);

}
