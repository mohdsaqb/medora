import React, { Component } from 'react'
// import * as alertify from 'alertifyjs';
// import "alertifyjs/build/css/themes/default.min.css";
// import "alertifyjs/build/css/themes/bootstrap.min.css"; 
// import "alertifyjs/build/css/alertify.min.css"; 
import { withRouter } from '../../withRouter';
import DateText from '../../components/DateText';
class PrescriptionDetail extends Component {
    constructor(props) {
        super(props)
        this.state = {
            prescriptionid: props.prescriptionid,
            detail: props.detail,
            drug_detail: props.drug_detail, 
            delivery_date: props.delivery_date,
            patientid:props.patientid,
            problemid:props.problemid
        };
    }  
    render() {
        return (
            <div>
                <div className="card" >
                    <div className="card-header"> <h3> Prescription Detail</h3>  </div>
                    <ul className="text-left list-group list-group-flush">
                        
                        <li className="list-group-item"><b>prescription id : </b>{this.props.prescriptionid}</li>
                        <li className="list-group-item"><b>Detail : </b>{this.props.detail}</li>
                        <li className="list-group-item"><b>Drug detail : </b>{this.props.drug_detail}</li>
                        <li className="list-group-item"><b>Usage : </b>{this.props.usage}</li>
                        <li className="list-group-item"><b>Barcode : </b>{this.props.barcode}</li>
                        <li className="list-group-item"><b>Delivery Date : </b>
                            {this.props.delivery_date !== null ?
                                <DateText value={this.props.delivery_date} withTime />
                            : null} 
                        </li> 
                    </ul>
                </div>
            </div>
        )
    }
}
export default withRouter(PrescriptionDetail)
