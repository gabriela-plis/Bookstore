import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import TextInput from "./inputs/TextInput";
import PasswordInput from "./inputs/PasswordInput";

const RegisterPage = () => {
    return ( 
    <main className="registerPage panel-wrapper">
        <WelcomeSection />
        <RegisterSection />
    </main> 
    );
}

const RegisterSection = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('')


    return (
        <section className="registerPage__register-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Create Account</h2>
            <form>
                <p className="register-section__form-fields">
                    <TextInput state={firstName} setState={setFirstName} isRequired={true} placeholder="First name"/>
                    <TextInput state={lastName} setState={setLastName} isRequired={true} placeholder="Last name"/>
                    <TextInput state={phone} setState={setPhone} isRequired={false} placeholder="Phone"/>
                    <TextInput state={email} setState={setEmail} isRequired={false} placeholder="Email"/>
                    <PasswordInput state={password} setState={setPassword} isRequired={true} placeholder="Password"/>
                </p>
                <button className="btn btn--register">Sign In</button>
            </form>
        </section>
    )
}

const WelcomeSection = () => {
    return (
        <section className="registerPage__welcome-section panel-wrapper__panel--image-background">
            <h2 className="panel-wrapper__title">Welcome!</h2>
            <p className="welcom-section__paragraph">A trip to the bookstore is the ultimate exercise in empathy. Within it you will find endless opportunities to see and understand the world from someone else’s point of view.</p>
            <Link to='/'>See Our Books</Link>
        </section>
    )
}


export default RegisterPage;