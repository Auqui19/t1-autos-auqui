package pe.edu.cibertec.t1_auqui_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.t1_auqui_frontend.dto.VehiculoRequest;
import pe.edu.cibertec.t1_auqui_frontend.dto.VehiculoResponse;
import pe.edu.cibertec.t1_auqui_frontend.model.VehiculoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscar")
    public String getBuscarVehiculo(Model model) {
        VehiculoModel vehiculoModel = new VehiculoModel("00", "", "", "", "", "", "");
        model.addAttribute("vehiculoModel", vehiculoModel);
        return "index";
    }

    @PostMapping("/buscar")
    public String postBuscarVehiculo(@RequestParam("placa") String placa, Model model) {
        if (placa == null || placa.trim().isEmpty() || placa.length() != 7) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "01", "Debe ingresar una placa correcta.", "", "", "", "", "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            return "index";
        }

        try {
            String endpoint = "http://localhost:8082/vehiculos";
            VehiculoRequest vehiculoRequest = new VehiculoRequest(placa);
            VehiculoResponse vehiculoResponse = restTemplate.postForObject(endpoint, vehiculoRequest, VehiculoResponse.class);

            if (vehiculoResponse != null && vehiculoResponse.codigo().equals("00")) {
                VehiculoModel vehiculoModel = new VehiculoModel(
                        "00", "",
                        vehiculoResponse.vehiculoMarca(),
                        vehiculoResponse.vehiculoModelo(),
                        vehiculoResponse.vehiculoNroAsientos(),
                        vehiculoResponse.vehiculoPrecio(),
                        vehiculoResponse.vehiculoColor());
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "vehiculodetalle";
            } else {
                VehiculoModel vehiculoModel = new VehiculoModel(
                        "01", "No se encontró un vehículo para la placa ingresada.", "", "", "", "", "");
                model.addAttribute("vehiculoModel", vehiculoModel);
                return "index";
            }
        } catch (Exception e) {
            VehiculoModel vehiculoModel = new VehiculoModel(
                    "99", "Error: Ocurrió un problema al buscar el vehículo.", "", "", "", "", "");
            model.addAttribute("vehiculoModel", vehiculoModel);
            logger.error("Error al buscar el vehículo: ", e);
            return "index";
        }
    }
}
