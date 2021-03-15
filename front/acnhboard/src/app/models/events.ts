export class Events {

    constructor (
        private _id?: number,
        private _name?: string,
        private _hostId?: number,
        private _hostName?: string,
        private _opening?: Date,
        private _ending?: Date,
        private _description?: string,
        private _guestLimit?: number,
        private _currentTotalGuests?: number,
        private _totalGuestLimit?: number,
        private _currentQueuePosition?: number,
        private _visibility?: string,
        private _eventStatus?: string,
        private _registrationStatus?: string,
        private _entryCode?: string
    ){}

    public get id(): number {
        return this._id;
    }

    public set id(value: number) {
        this._id = value;
    }

    public get name(): string {
        return this._name;
    }

    public set name(value: string) {
        this._name = value;
    }

    public get hostId(): number {
        return this._hostId;
    }

    public set hostId(value: number) {
        this._hostId = value;
    }

    public get hostName(): string {
        return this._hostName;
    }

    public set hostName(value: string) {
        this._hostName = value;
    }

    public get opening(): Date {
        return this._opening;
    }

    public set opening(value: Date) {
        this._opening = value;
    }

    public get ending(): Date {
        return this._ending;
    }

    public set ending(value: Date) {
        this._ending = value;
    }

    public get description(): string {
        return this._description;
    }

    public set description(value: string) {
        this._description = value;
    }

    public get guestLimit(): number {
        return this._guestLimit;
    }

    public set guestLimit(value: number) {
        this._guestLimit = value;
    }

    public get currentTotalGuests() {
        return this._currentTotalGuests;
    }
    
    public set currentTotalGuests(value) {
        this._currentTotalGuests = value;
    }

    public get totalGuestLimit(): number {
        return this._totalGuestLimit;
    }

    public set totalGuestLimit(value: number) {
        this._totalGuestLimit = value;
    }

    public get currentQueuePosition(): number {
        return this._currentQueuePosition;
    }
    
    public set currentQueuePosition(value: number) {
        this._currentQueuePosition = value;
    }

    public get visibility(): string {
        return this._visibility;
    }

    public set visibility(value: string) {
        this._visibility = value;
    }

    public get eventStatus(): string {
        return this._eventStatus;
    }

    public set eventStatus(value: string) {
        this._eventStatus = value;
    }

    public get registrationStatus(): string {
        return this._registrationStatus;
    }

    public set registrationStatus(value: string) {
        this._registrationStatus = value;
    }

    public get entryCode(): string {
        return this._entryCode;
    }
    
    public set entryCode(value: string) {
        this._entryCode = value;
    }
    
    
}