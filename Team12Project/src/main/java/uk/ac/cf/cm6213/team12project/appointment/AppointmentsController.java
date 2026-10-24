package uk.ac.cf.cm6213.team12project.appointment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cf.cm6213.team12project.emailReminder.emailService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class AppointmentsController {

    private final AppointmentService appointmentService;
    private final emailService emailService;

    public AppointmentsController(AppointmentService appointmentService, emailService emailService) {
        this.appointmentService = appointmentService;
        this.emailService = emailService;
    }

    @GetMapping("/appointments")
    public ModelAndView showAppointments() {
        ModelAndView mav = new ModelAndView("appointments/appointments-page");
        mav.addObject("appointment", new Appointment());
        mav.addObject("appointments", appointmentService.getAllAppointments());
        return mav;
    }

    @PostMapping("/appointments")
    public ModelAndView addAppointment(@Valid Appointment appointment, BindingResult result) {

        if(result.hasErrors()) {
            ModelAndView mav = new ModelAndView("appointments/appointments-page");
            mav.addObject("appointment", appointment);
            mav.addObject("appointments", appointmentService.getAllAppointments());
            return mav;
        }

        appointmentService.addAppointment(appointment);

        String to = appointment.getPatientEmail();
        String subject = "Lifestyle Medicine Appointment";
        String body = "Hi " + appointment.getPatientName() + ",\n\n"
                + "Your appointment has been booked for "
                + appointment.getDate() + " at " + appointment.getTime() + ".\n\n"
                + "Notes from clinician:\n" + appointment.getMessage() + "\n\n"
                + "Best wishes,\nLifestyle Medicine Team";

        emailService.sendEmail(to, subject, body);

        return new ModelAndView("redirect:/appointments");
    }

}
