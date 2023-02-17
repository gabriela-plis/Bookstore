import React from 'react';
import './App.css';
import Header from './Header';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Homepage from './Homepage';
import LogInPage from './LogInPage';
import RegisterPage from './RegisterPage';

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
            <Routes>
              <Route path="/" element={<Homepage />}></Route>
              <Route path="/logIn" element={<LogInPage />}></Route>
              <Route path="/register" element={<RegisterPage />}></Route>
            </Routes>
      </Router>
    </div>
  );
}

export default App;
