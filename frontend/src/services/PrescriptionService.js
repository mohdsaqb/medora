import ApiService from "./ApiService";


const PRESCRIPTION_BASE_URL = '/prescription';
const FIND_BY_R_ID = '/find-by-id/';
const FIND_ALL = /find-all-by-problemid/;

class PrescriptionService {

    getAllPrescriptions() {
        return ApiService.getAllDatas(PRESCRIPTION_BASE_URL);
    }

    getAllByProblemId(problemId) {
        return ApiService.getAll(PRESCRIPTION_BASE_URL + FIND_ALL + problemId);
    }

    getPrescriptionByPrescriptionId(prescriptionId) {
        return ApiService.getOneById(PRESCRIPTION_BASE_URL + FIND_BY_R_ID + prescriptionId);
    }

    // fetchPatientByEmail(email) {
    //     return axios.get(PATIENT_API_BASE_URL + '/find-by-email/' + email);
    // }

    deletePrescription(prescriptionId) {
        return ApiService.deleteById(PRESCRIPTION_BASE_URL + '/' + prescriptionId);
    }

    save(prescription) {
        return ApiService.post(PRESCRIPTION_BASE_URL, prescription);
    }

    // editPrescription(patient) {
    //     return ApiService.put(PRESCRIPTION_API_BASE_URL + '/' + patient.patientid, patient);
    // }
}

const prescriptionService = new PrescriptionService();
export default prescriptionService;
