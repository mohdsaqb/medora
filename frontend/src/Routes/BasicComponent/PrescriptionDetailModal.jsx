import React, { Component } from 'react'
// import * as alertify from 'alertifyjs';
// import "alertifyjs/build/css/themes/default.min.css";
// import "alertifyjs/build/css/themes/bootstrap.min.css"; 
// import "alertifyjs/build/css/alertify.min.css"; 
import { withRouter } from 'react-router';
import Moment from 'react-moment';

class PrescriptionDetailModal extends Component {
    constructor(props) {
        super(props)
        this.state = {
            prescriptionid: props.prescription.prescriptionid,
            detail: props.prescription.detail,
            drug_detail: props.prescription.drug_detail,
            delivery_date: props.prescription.delivery_date,
            patientid: props.prescription.patientid,
            problemid: props.prescription.problemid
        };
    }
    render() {
        return (<div className="modal fade" id="prescriptionModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div className="modal-dialog" role="document">
                <div className="modal-content">
                    <div className="modal-header">
                        <h3 className="modal-title" id="exampleModalLabel">Prescription Detail</h3>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        <div>
                            <div className="card" >
                                <div className="card-header"> <h3> Prescription Detail</h3>  </div>
                                <ul className="text-left list-group list-group-flush">

                                    <li className="list-group-item"><b>prescription id : </b>{this.props.prescription.prescriptionid}</li>
                                    <li className="list-group-item"><b>Detail : </b>{this.props.prescription.detail}</li>
                                    <li className="list-group-item"><b>Drug detail : </b>{this.props.prescription.drug_detail}</li>
                                    <li className="list-group-item"><b>Usage : </b>{this.props.prescription.usage}</li>
                                    <li className="list-group-item"><b>Barcode : </b>{this.props.prescription.barcode}</li>
                                    <li className="list-group-item"><b>Delivery Date : </b>
                                        {this.props.prescription.delivery_date !== null ?
                                            <Moment format="YYYY / MM / DD  HH:mm">
                                                {this.props.prescription.delivery_date}
                                            </Moment>
                                            : null}
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        )
    }
}
export default withRouter(PrescriptionDetailModal)
