import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputEntrycodeComponent } from './input-entrycode.component';

describe('InputEntrycodeComponent', () => {
  let component: InputEntrycodeComponent;
  let fixture: ComponentFixture<InputEntrycodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputEntrycodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputEntrycodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
