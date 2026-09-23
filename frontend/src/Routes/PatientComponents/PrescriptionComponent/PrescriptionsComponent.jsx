import React, { Component } from 'react'
import AlertifyService from '../../../services/AlertifyService';
import PrescriptionService from '../../../services/PrescriptionService';
import DateText from '../../../components/DateText';
import * as alertify from 'alertifyjs';
import "alertifyjs/build/css/alertify.css";
import "alertifyjs/build/css/themes/default.css";
import { withRouter } from '../../../withRouter';
import PrescriptionDetailModal from '../../BasicComponent/PrescriptionDetailModal';

class PrescriptionsComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {
            problemid: props.problemid,
            prescriptions: [],
            prescription: {}
        }
        this.getAllPrescriptions = this.getAllPrescriptions.bind(this); 
    }
    componentDidMount() {
        this.getAllPrescriptions();
    }
    getAllPrescriptions() {
        PrescriptionService.getAllByProblemId(this.state.problemid).then((res) => {
            this.setState({ prescriptions: res.data })
        }).catch((error) => {
            if (error.response) {
                AlertifyService.alert(error.response.data.message);
            }
            else if (error.request) console.log(error.request);
            else console.log(error.message);
        });
    }

    viewQuickly(r) {
        this.setState({ prescription: r })
    }
 
    deletePrescription(receipid) {
        alertify.confirm("Are you sure to delete the prescription.",
            ok => {
                PrescriptionService.deletePrescription(receipid).then((res) => {
                    if (res.data === true) {
                        AlertifyService.successMessage('Prescription was deleted.');
                        this.getAllPrescriptions();
                    }
                }).catch((error) => {
                    if (error.response) {
                        AlertifyService.alert(error.response.data.message);
                    }
                    else if (error.request) console.log(error.request);
                    else console.log(error.message);
                });
            },
            cancel => { AlertifyService.errorMessage('Cancel'); }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }
    render() {
        let prescriptions = this.state.prescriptions;
        return (
            <div className="row">
                <div className="col-lg-12">
                    <hr />
                    <p className="h3 d-flex justify-content-center">Prescriptions</p>
                    <hr />
                    <div className="table-responsive">
                        <table className="table table-bordered table-sm table-dark table-hover">
                            <thead>
                                <tr>
                                    <th>ID </th>
                                    <th>Prescription Detail</th>
                                    <th>Drug Detail</th>
                                    <th>Create Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {prescriptions.map(r =>

                                    <tr className="bg-default" key={r.prescriptionid}>
                                        <td>{r.prescriptionid}</td>
                                        <td>{r.detail}</td>
                                        <td>{r.drug_detail}</td>
                                        <td>
                                            <DateText value={r.delivery_date} withTime />
                                        </td>
                                        <td>
                                            <div className="btn-group" role="group">
                                                <button id="btnGroupDrop1"
                                                    type="button"
                                                    className="btn btn-sm btn-secondary dropdown-toggle"
                                                    data-toggle="dropdown"
                                                    aria-haspopup="true"
                                                    aria-expanded="false"> Actions </button>

                                                <div className="dropdown-menu" aria-labelledby="btnGroupDrop1">
                                                    <button
                                                        type="button"
                                                        className="dropdown-item"
                                                        onClick={() => this.viewQuickly(r)}
                                                        data-toggle="modal" data-target="#prescriptionModal" > 
                                                    View Quickly</button>
                                                    <div className="dropdown-divider"></div>
                                                    <button
                                                        className="dropdown-item"
                                                        onClick={() => this.deletePrescription(r.prescriptionid)} >
                                                        Delete </button>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>

                        <PrescriptionDetailModal prescription={this.state.prescription}/>
                        <hr />
                        <hr />
                        <hr />
                    </div>
                </div>
            </div>
        )
    }
}
export default withRouter(PrescriptionsComponent);