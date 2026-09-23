import React, { Component } from 'react'
import DateText from '../../../components/DateText';
import * as alertify from 'alertifyjs';
import "alertifyjs/build/css/alertify.css";
import "alertifyjs/build/css/themes/default.css";
import ProblemService from '../../../services/ProblemService';
import AlertifyService from '../../../services/AlertifyService';
import { withRouter } from '../../../withRouter';
import ProblemDetailModal from '../../BasicComponent/ProblemDetailModal';

let filterAllProblem = [];
let filters = ["problemName", "problemStatus"];
class ProblemsComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            patientid: props.patientid,
            problems: [],
            problem:{}

        }
        this.getAllProblems = this.getAllProblems.bind(this);
    }
    componentDidMount() {
        this.getAllProblems();
    }
    getAllProblems() {
        ProblemService.getAllByPatientId(this.state.patientid).then(res => {
            // Keep the unfiltered list so the search box has something to
            // filter against. It was previously declared but never assigned,
            // so every search returned nothing.
            filterAllProblem = res.data;
            this.setState({ problems: res.data });
        }).catch(() => {
            filterAllProblem = [];
            this.setState({ problems: [] });
        });
    }
    onChangeSearchByStatusOrDate = (e) => { this.filterProblems(e.target.value); }
    filterProblems(value) {
        var results = [];
        if (value !== '') {
            results = filterAllProblem.filter(problem => {
                let find = false;
                //filters.forEach(filter=>{
                filters.forEach(function (filter) {
                    let field = problem[filter];
                    if (field == null) return;
                    if (String(field).toLowerCase().indexOf(value.toLowerCase()) > -1) find = true;
                });
                return find;
            });
            this.setState({ problems: results });
        }
        else { this.setState({ problems: filterAllProblem }); }
    }
    limitingPatientDetail(data) {
        if (data.length < 31) return data;
        else return data.substr(0, 30) + "...";
    }
    deleteProblem(problemid) {
        alertify.confirm("Are you sure to delete the problem.",
            ok => {
                ProblemService.delete(problemid).then(res => {
                    //this.setState({ problems: this.state.problems.filter(p => p.problemid !== problemid) });
                    AlertifyService.successMessage('Deleting is ok : ');
                    this.getAllProblems();
                });
            },
            cancel => { AlertifyService.errorMessage('Cancel'); }
        ).set({ title: "Attention" }).set({ transition: 'slide' }).show();
    }
    viewProblem(problemid) {
        window.localStorage.setItem("problemid", problemid);
        this.props.history.push('/problem/' + problemid);
    }
    viewQuickly(problem){
        this.setState({problem:problem});
    }
    render() {
        let problems = this.state.problems;
        return (
            <div className="row">
            <div className="col-lg-12">
                <hr />
                <p className="h3 d-flex justify-content-center">Problems</p>
                <hr />
                <div className="form-group">
                    <input type="text"
                        placeholder="Search Problem by problem Name or problem Status"
                        name="searchByName"
                        className="form-control"
                        onChange={this.onChangeSearchByStatusOrDate}
                    />
                </div>
                <hr />
                <div className="table-responsive">
                    <table className="table table-bordered table-sm table-dark table-hover">
                        <thead>
                            <tr>
                                <th>Problem Name </th>
                                <th>Problem Detail</th>
                                <th>Problem Status</th>
                                <th>Create Date</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {problems.map(problem =>

                                <tr className="bg-default" key={problem.problemid}>
                                    <td>{problem.problemName}</td>
                                    <td>{this.limitingPatientDetail(problem.problemDetail)}</td>

                                    <td>{problem.problemStatus}</td>
                                    <td>
                                        <DateText value={problem.creationDate} withTime />
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
                                                    className="dropdown-item"
                                                    onClick={() => this.viewProblem(problem.problemid)} >
                                                    View </button>
                                                <div className="dropdown-divider"></div>
                                                <button
                                                    className="dropdown-item"
                                                    data-toggle="modal" data-target="#problemModal"
                                                    onClick={() => this.viewQuickly(problem)} >
                                                    View Quickly </button>
                                                <div className="dropdown-divider"></div>
                                                <button
                                                    className="dropdown-item"
                                                    onClick={() => this.deleteProblem(problem.problemid)} >
                                                    Delete </button>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                    <ProblemDetailModal problem={this.state.problem} />
                    <hr />
                    <hr />
                    <hr />
                </div>
            </div></div>
        )
    }
}
export default withRouter(ProblemsComponent);