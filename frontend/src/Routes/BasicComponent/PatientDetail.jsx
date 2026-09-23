import React, { Component } from 'react'
import * as alertify from 'alertifyjs';
import "alertifyjs/build/css/themes/default.min.css";
import "alertifyjs/build/css/themes/bootstrap.min.css";
import "alertifyjs/build/css/alertify.min.css";
import PatientService from '../../services/PatientService';
import { withRouter } from 'react-router';
import Moment from 'react-moment';

class PatientDetail extends Component {
    constructor(props) {
        super(props)
        this.state = {
            patientid: props.patientid,
            name: props.name,
            lastname: props.lastname,
            phoneNo: props.phoneNo,
            email: props.email,
            bornDate: props.bornDate,
            gender: props.gender,
            city: props.city,
            message: ''
        };
        // props.array.map(a => {
        //     console.log(a + ' : ' + props[a] + ' : ' + (typeof props[a]))
        // })
    }
    editPatient(id) {
        alertify.confirm(
            "Are you sure to edit this patient.",
            ok => {
                this.props.history.push(`/edit-patient/${id}`);
            },
            cancel => {
                alertify.error('Cancel');
            }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }
    deletePatient(patientid) {
        alertify.confirm("Are you sure to delete this patient.",
            function () {
                PatientService.deletePatient(patientid)
                    .then(res => {
                        window.location.href = '/patients';
                        alertify.success("Deleting is ok ");
                    })
            },
            function () {
                alertify.error('Cancel');
            }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }
    render() {
        var age = null;
        if (this.props.bornDate != null) {
            const born = new Date(this.props.bornDate);
            const today = new Date();
            age = today.getFullYear() - born.getFullYear();
            const monthDelta = today.getMonth() - born.getMonth();
            // Not had this year's birthday yet.
            if (monthDelta < 0 || (monthDelta === 0 && today.getDate() < born.getDate())) {
                age = age - 1;
            }
        }
        return (
            <div>
                <div className="card" >
                    <div className="card-header"> <h3> Patient Detail</h3>  </div>
                    <ul className="text-left list-group list-group-flush">
                        <li className="list-group-item"><b>Patient id : </b>{this.props.patientid}</li>
                        <li className="list-group-item"><b>Name : </b>{this.props.name}</li>
                        <li className="list-group-item"><b>Last Name : </b>{this.props.lastname}</li>
                        <li className="list-group-item"><b>Phone No : </b>{this.props.phoneNo}</li>
                        <li className="list-group-item"><b>Age : </b>
                            {age !== null ? age : null}
                        </li>
                        <li className="list-group-item"><b>Born Date : </b>
                            {this.props.bornDate !== null ?
                                <Moment date={this.props.bornDate} format="DD MMM YYYY" /> : null
                            }
                        </li>
                        <li className="list-group-item"><b>Email : </b>{this.props.email}</li>
                        <li className="list-group-item"><b>City : </b>{this.props.city}</li>
                        <li className="list-group-item"><b>Gender : </b>{this.props.gender}</li>
                        {this.props.showButtons?
                        <li className="list-group-item">
                            <button
                                className="btn btn-sm btn-success"
                                onClick={() => this.editPatient(this.props.patientid)} >
                                Edit
                            </button>
                            <button
                                className="btn btn-sm btn-danger"
                                onClick={() => this.deletePatient(this.props.patientid)}>
                                Delete
                            </button>
                        </li>
                         : null}
                    </ul>
                </div>
            </div>
        )
    }
}
export default withRouter(PatientDetail)
