import React, { Component } from 'react'
import PatientService from '../../services/PatientService';
import AuthService from '../../services/AuthService';
import * as alertify from 'alertifyjs';
import "alertifyjs/build/css/alertify.css";
import DatePicker from "react-datepicker";
import AlertifyService from '../../services/AlertifyService';

class AddPatientComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            lastname: '',
            email: '',
            phoneNo:'',
            gender: 'Male',
            city: 'DELHI',
            bornDate: new Date(),
            status: 1,
            cities: []
        }
        // this.saveUser = this.saveUser.bind(this);
        this.getAllCities();
    }
    getAllCities() {
        PatientService.getCities().then(res => {
            this.setState({ cities: res.data });

        });
    }
    /** True when a field marked with an asterisk is still empty. */
    controlQuickly() {
        const blank = value => value == null || String(value).trim() === '';
        // Phone is marked required in the form but was never checked here,
        // so records saved without it.
        return blank(this.state.name) || blank(this.state.lastname) || blank(this.state.phoneNo);
    }
    saveUser = (e) => {
        if (!this.controlQuickly()) {
            e.preventDefault();
            let patient = this.state;
            PatientService.addPatient(patient)
                .then(res => {
                    this.setState({ message: 'User added successfully.' });
                    this.props.history.push('/patients');
                    alertify.success("Adding patient is ok");
                }).catch((error) => {
                    const message = this.describeError(error);
                    this.setState({ errorMessage: message, patientid: null });
                    AlertifyService.alert(message);
                });
        } else
            AlertifyService.alert('Please fill in all fields marked with *');
    }
    /**
     * Turns a failed request into something worth showing.
     *
     * Reading error.response.data.message directly gave undefined whenever the
     * server answered without that field, and Alertify then displayed its own
     * "No message available" placeholder.
     */
    describeError(error) {
        if (!error.response) {
            return 'Could not reach the server. Check that the backend is running.';
        }
        const { status, data } = error.response;
        if (data && data.message) return data.message;
        if (status === 401) return 'Sign in to add a patient';
        if (status === 403) return 'You do not have permission to add patients';
        if (status === 409) return 'A patient with these details already exists';
        if (status === 400) return 'Please check the details entered and try again';
        return `Could not save the patient (error ${status})`;
    }

    componentDidMount() {
        // Creating a patient needs a signed in account. Without this the form
        // was reachable by URL and only failed at submit time with a 401.
        if (!AuthService.canEditPatients()) {
            AlertifyService.alert('Sign in to add a patient');
            this.props.history.push('/patients');
        }
    }

    onChangeData(type, data) {
        const stateData = this.state;
        stateData[type] = data;
        this.setState({ stateData });
    }
    back() {
        this.props.history.push('/patients');
    }
    render() {
        //let bornDate = this.state.bornDate;
        const isWeekday = date => {
            const day = date.getDay(date);
            return day !== 0 && day !== 6;
        };
        let { name, lastname,phoneNo, email, bornDate, gender, city } = this.state;
        return (
            <div className="row">
                <div className="col-sm-12">
                    <button
                        className="btn btn-danger"
                        onClick={() => this.back()}> Back </button>
                    <hr />
                </div>
                <div className="col-sm-8">
                    <h2 className="text-center">ADD PATIENT</h2>
                    <form>
                        <div className="form-group">
                            <label>Name *</label>
                            <input type="text" placeholder="name" name="name" className="form-control" value={name} onChange={e => this.onChangeData('name', e.target.value)} />
                        </div>
                        <div className="form-group">
                            <label>Last Name *</label>
                            <input placeholder="Last name" name="lastname" className="form-control" value={lastname} onChange={e => this.onChangeData('lastname', e.target.value)} />
                        </div>
                        <div className="form-group">
                            <label>Phone *</label>
                            <input placeholder="Last name" name="phone No" className="form-control" value={phoneNo} onChange={e => this.onChangeData('phoneNo', e.target.value)} />
                        </div>
                        <div className="form-group">
                            <label>Email:</label>
                            <input placeholder="Email" name="email" className="form-control" value={email} onChange={e => this.onChangeData('email', e.target.value)} />
                        </div>
                        <div className="form-group">
                            <label>Born Date *</label>
                            <div className="form-group">
                                <DatePicker
                                    className="form-control"
                                    // showTimeSelect
                                    showTimeInput
                                    selected={bornDate}
                                    onChange={e => this.onChangeData('bornDate', e)}
                                    filterDate={isWeekday}          // disable weekend
                                    timeIntervals={15}              // time range around 15 min
                                    //showWeekNumbers               // show week number
                                    timeFormat="HH:mm"              // show time format
                                    dateFormat="yyyy/MM/dd h:mm aa" // show all of time format
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Gender *</label>
                            <select className="form-control"
                                value={gender}
                                onChange={e => this.onChangeData('gender', e.target.value)} >
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>City *</label>
                            <select className="form-control"
                                value={city}
                                onChange={e => this.onChangeData('city', e.target.value)} >
                                {this.state.cities.map(city =>
                                    <option key={city} value={city}>{city}</option>
                                )}
                            </select>
                        </div>

                        <button className="btn btn-success" type="button" onClick={this.saveUser}>Save</button>
                    </form>
                </div>
                <div className="col"></div>
                <div className="col-lg-3">
                    <img style={{ height: 200 }} src="https://i1.wp.com/www.nosinmiubuntu.com/wp-content/uploads/2013/02/New-Database.png?w=770" alt="" />
                </div>
                <div className="col-sm-12">
                    <hr />
                    <hr />
                    <hr />
                </div>
            </div>
        );
    }
}

export default AddPatientComponent;