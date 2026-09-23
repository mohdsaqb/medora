import React from 'react';
import './App.css';
import { Routes, Route, BrowserRouter } from "react-router-dom";
import ListPatientComponent from './Routes/PatientComponents/ListPatientComponent';
import ViewPatientComponent from './Routes/PatientComponents/ViewPatientComponent';
import AddPatientComponent from './Routes/PatientComponents/AddPatientComponent';
import EditPatientComponent from './Routes/PatientComponents/EditPatientComponent';
import NotFoundComponent from './NotFound/NotFoundComponent';
import ViewProblemComponent from './Routes/PatientComponents/ProblemComponent/ViewProblemComponent';
import PrescriptionFormComponent from './Routes/PatientComponents/PrescriptionComponent/PrescriptionFormComponent';
import NavbarComponent from './Navbar/NavbarComponent';
import ProblemFormComponent from './Routes/PatientComponents/ProblemComponent/ProblemFormComponent';
// https://www.youtube.com/watch?v=DQ93TxqKkWo
function App() {
  return (            
    <div className="App" >
      <div className="container">
        <div className="row">
          <div className="col-sm-12">
          <NavbarComponent />
          <a href="/">
            {/* style={{width: 400, height: 100}}  */}
            <img style={{ height: "100px", margin: "10px 0"}}  
            src="https://www.phuketinternationalhospital.com/en/wp-content/themes/pih/images/logo-nonetext.png" alt="" />
          </a>
            <BrowserRouter>
              <Routes>
                <Route path="/" element={<ListPatientComponent />} />
                <Route path="/patients" element={<ListPatientComponent />} />
                <Route path="/view-patient/:patientid" element={<ViewPatientComponent />} />
                <Route path="/add-patient" element={<AddPatientComponent />} />
                <Route path="/edit-patient/:patientid" element={<EditPatientComponent />} />
                <Route path="/add-problem" element={<ProblemFormComponent />} />
                <Route path="/problem/:problemid" element={<ViewProblemComponent />} />
                <Route path="/prescription-form" element={<PrescriptionFormComponent />} />
                <Route path="/notfound" element={<NotFoundComponent />} />
                <Route path="*" element={<NotFoundComponent />} />
              </Routes>
            </BrowserRouter>
          </div>
        </div>
      </div>


    </div>
  );
}

export default App;