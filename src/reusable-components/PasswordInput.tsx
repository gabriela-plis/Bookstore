type PasswordInputProps = {
    name: string;
    state: string;
    setState: (e: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder: string;
  };


const PasswordInput = (props: PasswordInputProps) => {
    return (
        <input 
            type="password"
            name={props.name}
            required
            value={props.state ?? ''} 
            onChange={(e) => {props.setState(e)}}
            placeholder={props.placeholder?? ''}
        />
    )
}
 
export default PasswordInput;