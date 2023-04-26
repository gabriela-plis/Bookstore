import { useEffect, useState } from "react";
import User from "../../DTO/UserDTO";
import useFetch from "../../functions/useFetch";
import TextInput from "../../reusable-components/TextInput";
import Select from "react-select/dist/declarations/src/Select";
import PasswordInput from "../../reusable-components/PasswordInput";
import ResetPassword from "../../DTO/ResetPasswordDTO";



const Settings = () => {
    const userDTO = useFetch('http://localhost:8080/users/this') as unknown as User;

    const [user, setUser] = useState<User>({
        id: 0,
        firstName: "",
        lastName: "",
        phone: "",
        email: "",
        roles: []
    });
   
    const [readyToRender, setReadyToRender] = useState(false);


    useEffect( () => {

        if (Object.keys(userDTO).length !== 0) {

        setUser(userDTO);
        
        setReadyToRender(true);
        }

    },[userDTO])


    const handleEditData = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => {
        e.preventDefault();

       
        fetch('http://localhost:8080/users', {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(user)
            })

        setShowEditData(false);
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUser(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
    }

    return (
        <section className="settings">
            <h2 className="settings__title">Account settings</h2>
            {readyToRender &&  
                <div className="settings__content">
                    <DataArea data={user.firstName} setData={handleChange} className="first-name" inputName="firstName" displayName="First name" handleEditData={handleEditData}/>
                    <DataArea data={user.lastName} setData={handleChange} className="last-name" inputName="lastName" displayName="Last name" handleEditData={handleEditData}/>
                    <DataArea data={user.email} setData={handleChange} className="email" displayName="Email" handleEditData={handleEditData}/>
                    <DataArea data={user.phone} setData={handleChange} className="phone" displayName="Phone" handleEditData={handleEditData}/>
                    <ResetPasswordArea />
                </div>     
            }
            <span className="settings__image"><div></div></span>
        </section>
    )
}

type DataAreaProps = {
    data: string;
    setData: (e: React.ChangeEvent<HTMLInputElement>) => void;
    className: string;
    displayName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
    inputName?: string;
    buttonName?: string;
}

const DataArea = (props: DataAreaProps) => {

    const[showEditPanel, setShowEditPanel] = useState(false);

    const handleClickEditBtn = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        setShowEditPanel(true);
    }

    return (
        <div className = {"settings__data settings__data--"+props.className}>
            <label htmlFor={props.className}>
                <span className="settings__label-description">{props.displayName}:</span>
                {props.data ? (
                    <span>{props.data}</span>
                ) : (
                    <span>- - -</span>
                )} 
            </label>
            <button 
                className="btn btn--edit" 
                onClick={ (e) => {
                    handleClickEditBtn(e)
                }}
            >
               {props.buttonName ? (
                    <span>{props.buttonName}</span>
               ) : (
                    <span>Edit</span>
               )} 
            </button>
            {showEditPanel && (
                    <EditDataPanel 
                        data={props.data}
                        setData={props.setData} 
                        setShowEditPanel={setShowEditPanel} 
                        displayName={props.displayName} 
                        handleEditData={props.handleEditData} 
                        inputName={props.inputName ? props.inputName : props.className}
                    />
                )
            }
        </div>
    )
}

type EditDataPanelProps = {
    data: string;
    setData: (e: React.ChangeEvent<HTMLInputElement>) => void;
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
    displayName: string;
    inputName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
}

const EditDataPanel = (props: EditDataPanelProps) => {
    const {data, setData, setShowEditPanel, displayName, inputName, handleEditData} = {...props}

    const handleCancelOperation = () => {
        props.setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__background">
            <section className="edit__content">
                <h2 className="edit__title">Edit {displayName}</h2>
                <div className="edit__input-container">
                    <label className="edit__label">{displayName}</label>
                    <TextInput 
                        name={inputName}
                        state={data} 
                        setState={setData} 
                        isRequired 
                        placeholder={data}

                    />
                </div>
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        handleEditData(e, setShowEditPanel);
                    }}
                >Save</button>
            </section>
        </div>
     );
}



const ResetPasswordArea = () => {
    const[showEditPanel, setShowEditPanel] = useState(false)

    const handleResetOperation = () => {
        setShowEditPanel(true)
    }

    return (
        <div className="settings__data settings__data--password">
             <button className="btn" onClick={handleResetOperation}>Reset Password</button>
            {showEditPanel && (
                <ResetPasswordPanel
                    setShowEditPanel={setShowEditPanel}
                />
            )
            }
        </div> 
    )

}

type ResetPasswordPanelProps = {
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
}

const ResetPasswordPanel = (props: ResetPasswordPanelProps) => {
    const {setShowEditPanel} = {...props}

    const[passwords, setPasswords] = useState<ResetPassword>({
        currentPassword: "",
        newPassword: ""
    })

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPasswords(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 
    }

    const handleResetPassword = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => {
        e.preventDefault();

       
        fetch('http://localhost:8080/users/password', {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(passwords)
        })

        setShowEditData(false);
    }

    const handleCancelOperation = () => {
        setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__background">
            <section className="edit__content">
                <h2 className="edit__title">Reset Password</h2>
                <div className="edit__input-container">
                    <label className="edit__label">Current Password</label>
                    <PasswordInput 
                        name="current-password"
                        state={passwords.currentPassword} 
                        setState={handleChange} 
                    />
                    <label className="edit__label">New Password</label>
                    <PasswordInput
                        name="new-password"
                        state={passwords.newPassword} 
                        setState={handleChange} 
                    />
                </div>
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        handleResetPassword(e, props.setShowEditPanel);
                    }}
                >Save</button>
            </section>
        </div>
     );
}

export default Settings;