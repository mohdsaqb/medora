import React, { Component } from 'react'
import { withRouter } from '../../../withRouter';
import ProblemService from '../../../services/ProblemService'
//import DateText from '../../../components/DateText';
import PatientDetail from '../../BasicComponent/PatientDetail';
import ProblemDetail from '../../BasicComponent/ProblemDetail';
import AlertifyService from '../../../services/AlertifyService';
import PrescriptionsComponent from "../PrescriptionComponent/PrescriptionsComponent";




class ViewProblemComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {
            problemid: props.match.params.problemid,
            patient: {},
            prescriptions: [],
            problemDetail: null,
            problemName: null,
            problemStatus: null,
            pid: null,
            creationDate: null,
            errorMessage: ""
        }
        // this.loadProblemDetail();
        this.loadProblemDetail = this.loadProblemDetail.bind(this);
    }
    componentDidMount() {
        this.loadProblemDetail();
    }

    loadProblemDetail() {
        ProblemService.getProblem(this.state.problemid).then(res => {
            let p = res.data;
            this.setState({
                patient:p.patient,
                problemDetail:p.problemDetail,
                problemName:p.problemName,
                problemStatus:p.problemStatus,
                creationDate:p.creationDate,
                pid:p.pid,
            });
        }).catch((error) => {
            // Error
            if (error.response) {
                this.setState({ errorMessage: error.response.data.message, problemid: null });
                AlertifyService.alert(error.response.data.message);
                
            } else if (error.request) {
                console.log(error.request);
            } else {
                console.log(error.message);
            }
        });
    }
    viewPatient(patientid) {
        window.localStorage.setItem("patientId", patientid);
        this.props.history.push('/view-patient/' + patientid);
    }
    openPrescriptionForm(patientid, problemid) {
        window.localStorage.setItem("patientId", patientid);
        window.localStorage.setItem("problemId", problemid);
        this.props.history.push('/prescription-form');
    }
    render() {

        return (
            <div className="row">
                <div className="col-sm-12">
                    <h1>Problem Details</h1>
                    <hr />
                </div>
                <div className="col-sm-12">
                    <div className="row">
                        <div className="col-sm-12">
                            <button
                                className="btn btn-danger"
                                onClick={() => this.viewPatient(this.state.patient.patientid)}>
                                Back </button>
                            <button
                                className="btn btn-warning ml-1"
                                onClick={() => this.openPrescriptionForm(this.state.patient.patientid, this.state.problemid)} >
                                Add Prescription </button>
                            <hr />
                        </div>
                        <div className="col-lg-6">
                            <PatientDetail
                                name={this.state.patient.name}
                                lastname={this.state.patient.lastname}
                                email={this.state.patient.email}
                                city={this.state.patient.city}
                                bornDate={this.state.patient.bornDate}
                                gender={this.state.patient.gender}
                                patientid={this.state.patient.patientid}
                            />
                        </div>
                        <div className="col-lg-6">
                            <ProblemDetail
                                problemid={this.state.problemid}
                                problemName={this.state.problemName}
                                problemDetail={this.state.problemDetail}
                                problemStatus={this.state.problemStatus}
                                creationDate={this.state.creationDate}
                                patientid={this.state.patient.patientid}
                            />
                        </div>
                    </div>
                </div>
                <div className="col-sm-12">
                    <PrescriptionsComponent  problemid={this.state.problemid} />
                </div>
            </div>
        )
    }
}

export default withRouter(ViewProblemComponent);
