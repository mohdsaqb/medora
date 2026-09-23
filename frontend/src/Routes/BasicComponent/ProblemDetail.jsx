import React, { Component } from 'react'
import DateText from '../../components/DateText';
import { withRouter } from '../../withRouter';
class ProblemDetail extends Component {
    
    constructor(props) {
        super(props)
        this.state = {
            
        }
        this.openPrescriptionForm = this.openPrescriptionForm.bind(this);
    }

    openPrescriptionForm(patientid, problemid){
        window.localStorage.setItem("patientId", patientid);
        window.localStorage.setItem("problemId", problemid);
        this.props.history.push('/problem/prescription-form');
    }
    render() {
        return (
            <div>
                <div className="card" >
                    <div className="card-header"><h3> Problem Detail</h3> </div>
                    <ul className="text-left list-group list-group-flush">
                        <li className="list-group-item"><b>Problem Name : </b>{this.props.problemName}</li>
                        <li className="list-group-item"><b>Problem Detail : </b>{this.props.problemDetail}</li>
                        <li className="list-group-item"><b>Problem Status : </b>{this.props.problemStatus}</li>
                        <li className="list-group-item"><b>Creation Date (Y/M/D H/M) : </b>
                            <DateText value={this.props.creationDate} withTime />
                        </li>
                    </ul>
                </div>
            </div>
        )
    }
}
export default withRouter(ProblemDetail)