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

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const registerData = { firstName, lastName, phone, email, password };
        console.log("handle Submit")

        fetch('http://localhost:8000/users', {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(registerData)
        })
        .then( resp => { console.log(resp) });
    }

    return (
        <section className="registerPage__register-section panel-wrapper__panel--no-image-background">
            <h2 className="panel-wrapper__title">Create Account</h2>
            <form className="form" onSubmit={handleSubmit}>
                <p className="form__fields">
                    <TextInput state={firstName} setState={setFirstName} isRequired={true} placeholder="First name"/>
                    <TextInput state={lastName} setState={setLastName} isRequired={true} placeholder="Last name"/>
                    <TextInput state={email} setState={setEmail} isRequired={true} placeholder="Email"/>
                    <TextInput state={phone} setState={setPhone} isRequired={false} placeholder="Phone"/>
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
            <p className="panel-wrapper__paragraph panel-wrapper__paragraph--long-text">A trip to the bookstore is the ultimate exercise in empathy. Within it you will find endless opportunities to see and understand the world from someone else’s point of view.</p>
            <Link to='/' className="panel-wrapper__link">SEE OUR BOOKS</Link>
        </section>
    )
}


export default RegisterPage;