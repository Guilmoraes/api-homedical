package br.com.homedical.service.exceptions;


import static br.com.homedical.web.rest.errors.ApplicationErrorConstants.PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.web.rest.errors.ApplicationErrorConstants.USER_NOT_FOUND;

public enum ErrorConstants {

    ERR_CONCURRENCY_FAILURE {
        @Override
        public String getValue() {
            return "error.concurrencyFailure";
        }

        @Override
        public String getMessage() {
            return "Concurrency error in database access";
        }
    },

    ERR_VALIDATION {
        @Override
        public String getValue() {
            return "error.validation";
        }

        @Override
        public String getMessage() {
            return "Validation error (missing / invalid payload) for entity provided.";
        }
    },

    ERR_ACCESS_DENIED {
        @Override
        public String getValue() {
            return "error.accessDenied";
        }

        @Override
        public String getMessage() {
            return "You don't have access to this resource";
        }
    },

    ERR_METHOD_NOT_SUPPORTED {
        @Override
        public String getValue() {
            return "error.methodNotSupported";
        }

        @Override
        public String getMessage() {
            return "The method requested don't support the requested signature";
        }
    },

    ERR_INTERNAL_SERVER_ERROR {
        @Override
        public String getValue() {
            return "error.internalServerError";
        }

        @Override
        public String getMessage() {
            return "InternalServerError. Please ask for better info from the lazy dev";
        }
    },

    ERR_INVALID_PARAMS {
        @Override
        public String getValue() {
            return "error.invalidParams";
        }

        @Override
        public String getMessage() {
            return "Invalid params passed to the method.";
        }
    },
    ENTITY_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.entity.idnotfound";
        }

        @Override
        public String getMessage() {
            return "The provided ID is invalid for this resource or not found";
        }
    },
    ERROR_RESET_PASSWORD_EMAIL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.reset.password.email.not.found";
        }

        @Override
        public String getMessage() {
            return "Email not found";
        }
    },
    ERROR_SAVE_SPECIALTY_WITH_ID {
        @Override
        public String getValue() {
            return "error.save.specialty.with.id";
        }

        @Override
        public String getMessage() {
            return "";
        }
    },
    ERROR_UPDATE_SPECIALTY_ID_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.update.specialty.id.not.found";
        }

        @Override
        public String getMessage() {
            return "";
        }
    },
    S3_URL_NOT_AVAILABLE {
        @Override
        public String getValue() {
            return "error.s3.url";
        }

        @Override
        public String getMessage() {
            return "The URL for this resource could not be generated.";
        }
    },
    // Professional
    ERROR_PROFESSIONAL_EMAIL_ALREADY_USED {
        @Override
        public String getValue() {
            return "error.register.professional.email.already.used";
        }

        @Override
        public String getMessage() {
            return "Email already used!";
        }
    },
    ERROR_REGISTER_PROFESSIONAL_PASSWORD_MUST_BE_EQUAL {
        @Override
        public String getValue() {
            return "error.register.professional.password.must.be.equal";
        }

        @Override
        public String getMessage() {
            return "Password and ConfirmPassword must be equal!";
        }
    },
    ERROR_REGISTER_PROFESSIONAL_SPECIALTY_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.register.professional.specialty.not.found";
        }

        @Override
        public String getMessage() {
            return "Specialty not found!";
        }
    },
    ERROR_GET_PROFESSIONAL_ID_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.professional.id.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_UPDATE_PROFESSIONAL_ID_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.update.professional.id.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_GET_PROFESSIONAL_SELF_INFORMATION_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.constant.get.professional.self.information.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_CREATE_PATIENT_HEALTH_OPERATOR_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.create.patient.health.operator.not.found";
        }

        @Override
        public String getMessage() {
            return "Health Operator not found";
        }
    },
    ERROR_CREATE_PATIENT_CITY_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.create.patient.city.not.found";
        }

        @Override
        public String getMessage() {
            return "City not found";
        }
    },
    ERROR_DELETE_PATIENT_NOT_ALLOWED {
        @Override
        public String getValue() {
            return "error.delete.patient.not.allowed";
        }

        @Override
        public String getMessage() {
            return "Patient has relationship with other entities and cannot be deleted";
        }
    },
    ERROR_GET_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.professional.patients.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_UPDATE_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.update.professional.patients.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_UPDATE_PROFESSIONAL_PATIENTS_PATIENT_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.update.professional.patients.patient.not.found";
        }

        @Override
        public String getMessage() {
            return "Patient not found";
        }
    },
    ERROR_NAME_DUTY_EMPTY {
        @Override
        public String getValue() {
            return "error.duty.name.empty";
        }

        @Override
        public String getMessage() {
            return "Name of Duty is empty.";
        }

    },
    ERROR_TIME_START_DUTY_EMPTY {
        @Override
        public String getValue() {
            return "error.duty.start.empty";
        }

        @Override
        public String getMessage() {
            return "Time Start of Duty is empty.";
        }
    },
    ERROR_TIME_FINISH_DUTY_EMPTY {
        @Override
        public String getValue() {
            return "error.duty.finish.empty";
        }

        @Override
        public String getMessage() {
            return "Time Finish of Duty is empty.";
        }
    }, ERROR_ID_DUTY_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.id.duty.not.found";
        }

        @Override
        public String getMessage() {
            return "Id Duty not found.";
        }
    },
    ERROR_TIME_START_SCHEDULE_EMPTY {
        @Override
        public String getValue() {
            return "error.schedule.start.empty";
        }

        @Override
        public String getMessage() {
            return "Time Start of Schedule is empty.";
        }
    },
    ERROR_TIME_FINISH_SCHEDULE_EMPTY {
        @Override
        public String getValue() {
            return "error.schedule.finish.empty";
        }

        @Override
        public String getMessage() {
            return "Time Finish of Schedule is empty.";
        }
    },
    ERROR_DUTY_EMPTY_ON_SCHEDULE {
        @Override
        public String getValue() {
            return "error.schedule.duty.empty";
        }

        @Override
        public String getMessage() {
            return "Duty is empty on Schedule";
        }
    }, ERROR_PATIENT_EMPTY_ON_SCHEDULE {
        @Override
        public String getValue() {
            return "error.schedule.patient.empty";
        }

        @Override
        public String getMessage() {
            return "Patient is empty on Schedule";
        }
    }, ERROR_PROFESSIONAL_EMPTY_ON_SCHEDULE {
        @Override
        public String getValue() {
            return "error.schedule.duty.professional";
        }

        @Override
        public String getMessage() {
            return "Professional is empty on Schedule";
        }
    }, ERROR_SCHEDULE_ID_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.schedule.id.not.found";
        }

        @Override
        public String getMessage() {
            return "Schedule id not found";
        }
    }, ERROR_SCHEDULE_APPROVED_DONT_UPDATE {
        @Override
        public String getValue() {
            return "error.cannot.update.approved.schedule.update";
        }

        @Override
        public String getMessage() {
            return "Cannot update approved schedule";
        }
    }, ERROR_ID_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.professional.id.not.found";
        }

        @Override
        public String getMessage() {
            return "Id Professional not found";
        }
    },
    ERROR_SCHEDULE_NOT_PENDING {
        @Override
        public String getValue() {
            return "error.schedule.not.pending";
        }

        @Override
        public String getMessage() {
            return "Schedule not pending";
        }
    },
    ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.pending.document.types.for.professional.user.not.found";
        }

        @Override
        public String getMessage() {
            return USER_NOT_FOUND;
        }
    },
    ERROR_GET_PENDING_DOCUMENT_TYPES_FOR_PROFESSIONAL_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.pending.document.types.for.professional.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_UPLOAD_DOCUMENT_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.upload.document.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_UPLOAD_DOCUMENT_TYPE_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.upload.document.type.not.found";
        }

        @Override
        public String getMessage() {
            return "Document Type not found";
        }
    },
    ERROR_GET_PROFESSIONAL_DOCUMENTS_USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.professional.documents.user.not.found";
        }

        @Override
        public String getMessage() {
            return "User not found;";
        }
    },
    ERROR_GET_PROFESSIONAL_DOCUMENTS_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.get.professional.documents.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.check.if.professional.have.documents.waiting.for.approvement.user.not.found";
        }

        @Override
        public String getMessage() {
            return USER_NOT_FOUND;
        }
    },
    ERROR_CHECK_IF_PROFESSIONAL_HAVE_DOCUMENTS_WAITING_FOR_APPROVEMENT_PROFESSIONAL_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.check.if.professional.have.documents.waiting.for.approvement.professional.not.found";
        }

        @Override
        public String getMessage() {
            return PROFESSIONAL_NOT_FOUND;
        }
    },
    ERROR_CHECK_IF_PROFESSIONAL_HAD_ACCEPTED_TERMS_USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.check.if.professional.had.accepted.terms.user.not.found";
        }

        @Override
        public String getMessage() {
            return USER_NOT_FOUND;
        }
    },
    ERROR_PROFESSIONAL_ACCEPT_TERM_USER_NOT_FOUND {
        @Override
        public String getValue() {
            return "error.professional.accept.term.user.not.found";
        }

        @Override
        public String getMessage() {
            return USER_NOT_FOUND;
        }
    };

    public abstract String getValue();

    public abstract String getMessage();

}
