export class Guest {

    constructor(
        private _username?: string,
        private _status?: string,
        private _queuePosition?: number
    ){}
    
    public get username(): string {
        return this._username;
    }
    
    public set username(value: string) {
        this._username = value;
    }

    public get status(): string {
        return this._status;
    }

    public set status(value: string) {
        this._status = value;
    }

    public get queuePosition(): number {
        return this._queuePosition;
    }

    public set queuePosition(value: number) {
        this._queuePosition = value;
    }
}
