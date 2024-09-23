package pe.edu.cibertec.t1_auqui_frontend.dto;

public record VehiculoResponse(
        String codigo,
        String mensaje,
        String vehiculoMarca,
        String vehiculoModelo,
        String vehiculoNroAsientos,
        String vehiculoPrecio,
        String vehiculoColor
) {
}
